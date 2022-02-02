package com.lyit.csd;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple class to demonstrate using HTTP requests on GitHub REST API to get public information on
 * a user and process the JSON data returned.
 */
public class Main {

  /**
   * Main entry point of the application.
   *
   * @param args String of arguments for the console application.
   */
  public static void main(String[] args) {
    // write your code here

    getUserRepos("docmilo");
    getUserDetails("docmilo");

  }


  /**
   * getUserRepos  calls the GitHub API endpoint user/repos and gets a list of public repositories
   * for the named user.
   *
   * @param userName a GitHub handle of the user we want to list their repositories for.
   */
  public static void getUserRepos(String userName) {

    //Defining the url we want to request data from
    String urlEndpoint = "https://api.github.com/users/" + userName + "/repos";

    OkHttpClient httpClient;
    String apiKey = ServiceProvider.getAPIKey();
    Response response = null;
    String responseData = null;

    httpClient = new OkHttpClient().newBuilder().build();
    Request request = new Request.Builder()
        //.url("https://api.github.com/users/docmilo/repos")
        .url(urlEndpoint)
        .method("GET", null)
        .build();
    try {
      response = httpClient.newCall(request).execute();
      /* First check to see if we got a valid response from the GitHub endpoint. If we did then
      we will  get a string containing an array of repositories. We need to convert this to a
      JSONArray so that we can extract the useful information */

      //Confirm the response codes
      if (response.code() == 200) {
        /* We have a valid response, just converting the response data to a string so that we can
        print it out and have a look at it
         */
        responseData = response.body().string();
        response.close(); //Good idea to close the response to avoid leaking resources
        System.out.println(responseData);

        //Gson gson = new Gson();
        //gson.toJson(responseData);
        JSONArray repositories = new JSONArray(responseData);

        System.out.println(
            "The user " + userName + " has: " + repositories.length() + " GitHub respositories");

        //Now lets do something with the repository data in the
        getRepoNames(repositories);

      }
    } catch (Exception e) {
      //  Block of code to handle errors
    }
  }

  /**
   * Prints out the names of the repositories.
   *
   * @param userRepositories a JSONArray containing information on the public repositories of the
   *                         specified user.
   */
  public static void getRepoNames(JSONArray userRepositories) {

    /* org.json.JSONArray implements a raw iterator so each element is considered to be an Object
     * and we can therefore use the for below and cast each element to a JSONObject */

    for (Object obj : userRepositories) {
      if (obj instanceof JSONObject) {
        System.out.println(((JSONObject) obj).getString("name"));
      }
    }
  }

  /**
   * getUserDetails prints out the details of the authorised gitHub user.
   */

  private static void getUserDetails(String userName) {
    //Defining the url we want to request data from
    String urlEndpoint = "https://api.github.com/users/" + userName;

    OkHttpClient httpClient;
    String apiKey = ServiceProvider.getAPIKey();
    Response response = null;
    String responseData = null;

    httpClient = new OkHttpClient().newBuilder().build();
    Request request = new Request.Builder()

        .url(urlEndpoint)
        .method("GET", null)
        .build();
    try {

      response = httpClient.newCall(request).execute();
      /* First check to see if we got a valid response from the GitHub endpoint. If we did then
      we will  get a string containing an array of repositories. We need to convert this to a
      JSONArray so that we can extract the useful information */

      //Confirm the response codes
      if (response.code() == 200) {
        /* We have a valid response, just converting the response data to a string so that we can
        print it out and have a look at it
         */
        responseData = response.body().string();
        response.close(); //Good idea to close the response to avoid leaking resources
        System.out.println(responseData);

        JSONObject userData = new JSONObject(responseData);
        String userCompany = userData.getString("bio");
        System.out.println(userName + " GitHub bio " + userCompany);

      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


  }

}

