/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedyang.addressbook;
import static spark.Spark.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.settings.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
/**
 *
 * @author tedyang
 */
public class Application {
    private static final ContactService service = new ContactService();
    private static ObjectMapper om = new ObjectMapper();
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        port(8080);
        
        //CHANGE THIS VALUE TO CONTROL THE ES PORT;
        int eSPort = 9200;
        
        RestHighLevelClient client = new RestHighLevelClient(
        RestClient.builder(
                new HttpHost("localhost", eSPort, "http")));
        
        /**
         * Basic landing page.
         */
        get("/", (req, res) -> "Welcome to the Address Book");
        
        /**
         * Gets a contact by name.
         */
        get("/contact/:id", "application/json", (req, res) -> {
            User user = service.getUserByName(req.params(":id"));
            
            try {
                GetRequest request = new GetRequest("addressbook", "contact", req.params(":id"));
                GetResponse getResponse = client.get(request);
                return getResponse;
            } 
            catch (ElasticsearchException exception) {
                if (exception.status() == RestStatus.CONFLICT) {
                    return "Something went wrong in GET";
                }
                else {
                    return "something else went wrong in GET";
                }
            }
        });
        
        /**
         * Gets contacts by a search query and specified page size and page index.
         */
        get("/contact", "application/json", (req, res) -> {
            String pageSize = req.queryParams("pageSize");
            String page = req.queryParams("page");
            int pageNumber = Integer.parseInt(page);
            String query = req.queryParams("query");
            
            SearchRequest searchRequest = new SearchRequest("addressbook");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
            searchSourceBuilder.size(Integer.parseInt(pageSize));
            searchSourceBuilder.from(pageNumber);
            searchRequest.source(searchSourceBuilder);
            //searchRequest.scroll(TimeValue.timeValueMinutes(1L));
            
            SearchResponse searchResponse = client.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            
            return searchResponse;
        });
        
        /**
         * Creates a contact with name, address, and phone as params.
         */
        post("/contact", "application/json", (req, res) -> {
            String name = req.queryParams("name");
            String address = req.queryParams("address");
            String phoneNumber = req.queryParams("phone");
            User user = service.addUser(name, phoneNumber, address);
            
            IndexRequest request = new IndexRequest("addressbook", "contact", name)
                .source("name", name, 
                        "address", address, 
                        "phoneNumber", phoneNumber);
            try {
                IndexResponse response = client.index(request);
                return response;
            } catch(ElasticsearchException e) {
                if (e.status() == RestStatus.CONFLICT) {
                    return "HELLO SOMETHING WENT WRONG" + e.getDetailedMessage();
                }
                else {
                    return e.getDetailedMessage();
                }
            }
        });
        
        /**
         * Updates a contact with a given name with the address and phone in params
         */
        put("/contact/:id", "application/json", (req, res) -> {
            String address = req.queryParams("address");
            String phoneNumber = req.queryParams("phone");
            User user = service.updateUser(req.params(":id"), phoneNumber, address);
            UpdateRequest request = new UpdateRequest(
                "addressbook", 
                "contact",  
                req.params(":id")).doc("address", address,
                        "phoneNumber", phoneNumber);
            try {
                UpdateResponse updateResponse = client.update(request);
                return updateResponse;
            } catch(ElasticsearchException e) {
                if (e.status() == RestStatus.CONFLICT) {
                    return "Something went wrong with PUT";
                }
                else {
                    return e.getDetailedMessage();
                }
            }
        });
        
        /**
         * Deletes a contact by name
         */
        delete("/contact/:id", "application/json", (req, res) -> {
            User user = service.getUserByName(req.params(":id"));
            if (user != null) {
                service.deleteUser(req.params(":id"));
            }
            DeleteRequest request = new DeleteRequest(
                "addressbook",    
                "contact",     
                req.params(":id"));
            try {
                DeleteResponse deleteResponse = client.delete(request);
                return deleteResponse;
            } catch (IOException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                return "Error deleting a user";
            }
        });
    }
}
