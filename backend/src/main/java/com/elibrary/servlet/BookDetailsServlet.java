package com.elibrary.servlet;

import com.elibrary.model.BookDetail;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BookDetailsServlet - Hybrid aggregator using Google Books API and OpenLibrary as fallback.
 * Endpoint: /api/book/details?isbn=XXXX  OR ?q=search+term
 *
 * This servlet does simple in-memory caching to avoid repeated external calls.
 */
@WebServlet(urlPatterns = "/api/book/details")
public class BookDetailsServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    // Simple thread-safe cache: key -> BookDetail JSON
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    private String fetchUrl(String u) throws IOException {
        URL url = URI.create(u).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(7000);
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                status >= 200 && status < 400 ? con.getInputStream() : con.getErrorStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) sb.append(line);
        in.close();
        return sb.toString();
    }

    private BookDetail parseGoogle(JsonObject item){
        try{
            BookDetail d = new BookDetail();
            JsonObject vi = item.has("volumeInfo") ? item.getAsJsonObject("volumeInfo") : null;
            JsonObject sale = item.has("saleInfo") ? item.getAsJsonObject("saleInfo") : null;
            if(vi!=null){
                d.setTitle(vi.has("title") ? vi.get("title").getAsString() : null);
                if(vi.has("authors")){
                    JsonArray a = vi.getAsJsonArray("authors");
                    String[] auth = new String[a.size()];
                    for(int i=0;i<a.size();i++) auth[i] = a.get(i).getAsString();
                    d.setAuthors(auth);
                }
                if(vi.has("industryIdentifiers")){
                    JsonArray ids = vi.getAsJsonArray("industryIdentifiers");
                    for(JsonElement el : ids){
                        JsonObject o = el.getAsJsonObject();
                        if(o.has("type") && o.get("type").getAsString().toLowerCase().contains("isbn")){
                            d.setIsbn(o.get("identifier").getAsString()); break;
                        }
                    }
                }
                d.setDescription(vi.has("description") ? vi.get("description").getAsString() : null);
                d.setPublisher(vi.has("publisher") ? vi.get("publisher").getAsString() : null);
                d.setPageCount(vi.has("pageCount") ? vi.get("pageCount").getAsInt() : null);
                if(vi.has("imageLinks") && vi.getAsJsonObject("imageLinks").has("thumbnail"))
                    d.setThumbnail(vi.getAsJsonObject("imageLinks").get("thumbnail").getAsString());
            }
            // price if available in saleInfo.listPrice
            if(sale!=null && sale.has("listPrice") && sale.getAsJsonObject("listPrice").has("amount")){
                double p = sale.getAsJsonObject("listPrice").get("amount").getAsDouble();
                d.setPrice(p);
                d.setSource("google");
            } else {
                d.setSource("google");
            }
            return d;
        }catch(Exception ex){ return null; }
    }

    private BookDetail parseOpenLibrary(JsonObject root){
        try{
            BookDetail d = new BookDetail();
            if(root.has("docs")){
                JsonArray docs = root.getAsJsonArray("docs");
                if(docs.size()>0){
                    JsonObject first = docs.get(0).getAsJsonObject();
                    d.setTitle(first.has("title") ? first.get("title").getAsString() : null);
                    if(first.has("author_name")){
                        JsonArray a = first.getAsJsonArray("author_name");
                        String[] auth = new String[a.size()];
                        for(int i=0;i<a.size();i++) auth[i] = a.get(i).getAsString();
                        d.setAuthors(auth);
                    }
                    if(first.has("isbn")){
                        JsonArray is = first.getAsJsonArray("isbn");
                        if(is.size()>0) d.setIsbn(is.get(0).getAsString());
                    }
                    d.setPublisher(first.has("publisher") ? (first.get("publisher").isJsonPrimitive() ? first.get("publisher").getAsString() : first.getAsJsonArray("publisher").get(0).getAsString()) : null);
                    d.setDescription(first.has("first_sentence") ? first.get("first_sentence").getAsString() : null);
                    d.setSource("openlibrary");
                }
            }
            return d;
        }catch(Exception ex){ return null; }
    }

    private double estimatePrice(BookDetail d){
        // Basic heuristic estimator (USD):
        // If pageCount known -> base + pageCount * 0.15
        double base = 8.0; // base dollars
        if(d==null) return base;
        if(d.getPrice()!=null) return d.getPrice();
        if(d.getPageCount()!=null && d.getPageCount()>0){
            double p = base + d.getPageCount() * 0.15;
            return Math.round(p*100.0)/100.0;
        }
        int tlen = d.getTitle()!=null ? d.getTitle().length() : 20;
        int auth = d.getAuthors()!=null ? d.getAuthors().length : 1;
        double p = base + (tlen/10.0) + auth*2.0;
        return Math.round(p*100.0)/100.0;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String isbn = req.getParameter("isbn");
        String q = req.getParameter("q");
        String key = (isbn!=null? "isbn:"+isbn : (q!=null? "q:"+q : "all"));
        // Try cache
        if(CACHE.containsKey(key)){
            resp.setContentType("application/json");
            resp.getWriter().write(CACHE.get(key));
            return;
        }

        BookDetail result = null;

        try{
            if(isbn!=null && !isbn.isEmpty()){
                // Query Google Books by ISBN
                String google = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + URLEncoder.encode(isbn, StandardCharsets.UTF_8);
                String gresp = fetchUrl(google);
                JsonObject gjson = JsonParser.parseString(gresp).getAsJsonObject();
                if(gjson.has("items")){
                    result = parseGoogle(gjson.getAsJsonArray("items").get(0).getAsJsonObject());
                }
                // fallback OpenLibrary
                if(result==null || (result.getDescription()==null && (result.getThumbnail()==null)) ){
                    String ol = "https://openlibrary.org/search.json?isbn=" + URLEncoder.encode(isbn, StandardCharsets.UTF_8);
                    String ores = fetchUrl(ol);
                    JsonObject ojson = JsonParser.parseString(ores).getAsJsonObject();
                    BookDetail od = parseOpenLibrary(ojson);
                    if(result==null) result = od;
                    else {
                        if(result.getDescription()==null) result.setDescription(od.getDescription());
                        if(result.getThumbnail()==null) result.setThumbnail(od.getThumbnail());
                    }
                }
            } else if(q!=null && !q.isEmpty()){
                String google = "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(q, StandardCharsets.UTF_8) + "&maxResults=5";
                String gresp = fetchUrl(google);
                JsonObject gjson = JsonParser.parseString(gresp).getAsJsonObject();
                if(gjson.has("items")){
                    result = parseGoogle(gjson.getAsJsonArray("items").get(0).getAsJsonObject());
                }
                if(result==null){
                    String ol = "https://openlibrary.org/search.json?q=" + URLEncoder.encode(q, StandardCharsets.UTF_8) + "&limit=5";
                    String ores = fetchUrl(ol);
                    JsonObject ojson = JsonParser.parseString(ores).getAsJsonObject();
                    result = parseOpenLibrary(ojson);
                }
            } else {
                // no params => return bad request
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Provide isbn or q");
                return;
            }
        } catch(Exception ex){
            // ignore errors - we'll still attempt to return what we have
        }

        if(result==null) result = new BookDetail();
        // set isbn if missing from path param
        if((result.getIsbn()==null || result.getIsbn().isEmpty()) && isbn!=null) result.setIsbn(isbn);
        // estimate price when missing
        if(result.getPrice()==null) result.setPrice(estimatePrice(result));
        String out = gson.toJson(result);
        CACHE.put(key, out);
        resp.setContentType("application/json");
        resp.getWriter().write(out);
    }
}
