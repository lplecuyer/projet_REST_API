package ca.ulaval.glo2003;

import ca.ulaval.glo2003.assemblers.SellerAssembler;
import ca.ulaval.glo2003.models.Seller;
import ca.ulaval.glo2003.repositories.SellerRepository;

public class ServerApplication {

  private SellerRepository sellerRepository = new SellerRepository();

  public ServerApplication(){

  }

  public SellerAssembler.SellerResponse addSeller(SellerAssembler.SellerRequest sellerRequest){
    Seller seller = new Seller(sellerRequest.name(),
        sellerRequest.birthdate(),
        sellerRequest.email(),
        sellerRequest.phoneNumber(),
        sellerRequest.bio());
    sellerRepository.save(seller);
    return SellerAssembler.makeSellerResponse(seller);
  }

  public SellerAssembler.SellerResponse getSeller(String id){
    Seller seller = sellerRepository.get(id);
    if(seller != null)
      return SellerAssembler.makeSellerResponse(seller);
    else return null;
  }

  public void setSellerRepository(SellerRepository sellerRepository){
    this.sellerRepository =  sellerRepository;
  }
}
