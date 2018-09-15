package hw6;

public class Business {
  String businessID;
  String businessName;
  String businessAddress;
  String reviews;
  int reviewCharCount;
  
  public Business (String businessID, String businessName, String businessAddress, String reviews, int reviewCharCount) {
	  this.businessID = businessID; 
	  this.businessName = businessName; 
	  this.businessAddress = businessAddress; 
	  this.reviews = reviews; 
	  this.reviewCharCount = reviewCharCount; 
  }
  
  public String toString() {
    return "-------------------------------------------------------------------------------\n"
          + "Business ID: " + businessID + "\n"
          + "Business Name: " + businessName + "\n"
          + "Business Address: " + businessAddress + "\n"
          + "Character Count: " + reviewCharCount;
  }
  
  public int getReviewCharCount() {
	  return reviewCharCount; 
  }
}
