package ca.ulaval.glo2003.models;

public enum ProductCategory {
    beauty,
    sports,
    electronics,
    apparel,
    housing;

  public static boolean categoryExists(String stringCategory){
      boolean exists = true;
      try{
        ProductCategory.valueOf(stringCategory);
      }catch(IllegalArgumentException e){
        exists = false;
      }
      return exists;
  }
}
