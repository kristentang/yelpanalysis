# Yelp Analysis

Program in Computing 20A: Principles of Java with Applications\
Professor Ernest Ryu\
Homework Assignment\
March 2018

In this assignment, a Yelp data set was processed in order to efficiently extract meaningful words that represent the businesses. yelpDatasetParsed_full.txt contains Yelp reviews from 85,538 (out of their 66,000,000) business listings and was the data set used in the assignment. yelpDatasetParsed_short.txt is the sample data provided here. Starter code was provided for yelpAnalysis.java and Business.java. 

## Results

Meaningful words that  represent each business are determined by their term frequency-inverse document frequency (td-if) score: number of times a specific word appears in reviews for a document / number of documents in the entire set of reviews for all restaurants that contain the specific word. The function prints the 30 words with the highest td-if score the 10 businesses with the longest reviews. 
<img src = "https://kristentang.github.io/photos/yelp.jpg">
