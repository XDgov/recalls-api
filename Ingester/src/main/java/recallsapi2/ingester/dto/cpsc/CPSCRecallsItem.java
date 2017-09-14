package recallsapi2.ingester.dto.cpsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ferru001
 */
public class CPSCRecallsItem {

    @JsonProperty("RecallID")
    private String RecallID;
    @JsonProperty("RecallNumber")
    private String RecallNumber;
    @JsonProperty("RecallDate")
    private Date RecallDate;
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("URL")
    private String URL;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("ConsumerContact")
    private String ConsumerContact;
    @JsonProperty("LastPublishDate")
    private Date LastPublishDate;
    @JsonProperty("Products")
    private List<CPSCRecallsItem.CPSCRecallProduct> Products;
    @JsonProperty("Inconjunctions")
    private List<CPSCRecallsItem.Inconjunctions> Inconjunctions;
    @JsonProperty("Images")
    private List<CPSCRecallsItem.Image> Images;
    @JsonProperty("Injuries")
    private List<CPSCRecallsItem.Injury> Injuries;
    @JsonProperty("Manufacturers")
    private List<CPSCRecallsItem.Manufacturer> Manufacturers;
    @JsonProperty("ManufacturerCountries")
    private List<CPSCRecallsItem.ManufacturerCountry> ManufacturerCountries;
    @JsonProperty("ProductUPCs")
    private List<CPSCRecallsItem.ProductUPC> ProductUPCs;
    @JsonProperty("Hazards")
    private List<CPSCRecallsItem.Hazard> Hazards;
    @JsonProperty("Remedies")
    private List<CPSCRecallsItem.Remedy> Remedies;
    @JsonProperty("Retailers")
    private List<CPSCRecallsItem.Retailer> Retailers;

    public List<String> unwindInjuries() {
        List<String> unwoundInjuries = new ArrayList<>();
        for (Injury injury : Injuries) {
            unwoundInjuries.add(injury.getName());
        }
        return unwoundInjuries;
    }

    public List<String> unwindRemedies() {
        List<String> unwoundRemedies = new ArrayList<>();
        for (Remedy remedy : Remedies) {
            unwoundRemedies.add(remedy.getName());
        }
        return unwoundRemedies;
    }

    public static class CPSCRecallProduct {

        @JsonProperty("Name")
        private String Name;
        @JsonProperty("Description")
        private String Description;
        @JsonProperty("Model")
        private String Model;
        @JsonProperty("Type")
        private String Type;
        @JsonProperty("CategoryID")
        private String CategoryID;
        @JsonProperty("NumberOfUnits")
        private String NumberOfUnits;

        public CPSCRecallProduct() {
        }

        public CPSCRecallProduct(String Name, String Description, String Model, String Type, String CategoryID, String NumberOfUnits) {
            this.Name = Name;
            this.Description = Description;
            this.Model = Model;
            this.Type = Type;
            this.CategoryID = CategoryID;
            this.NumberOfUnits = NumberOfUnits;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getModel() {
            return Model;
        }

        public void setModel(String Model) {
            this.Model = Model;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getCategoryID() {
            return CategoryID;
        }

        public void setCategoryID(String CategoryID) {
            this.CategoryID = CategoryID;
        }

        public String getNumberOfUnits() {
            return NumberOfUnits;
        }

        public void setNumberOfUnits(String NumberOfUnits) {
            this.NumberOfUnits = NumberOfUnits;
        }
    }

    public static class Inconjunctions {

        @JsonProperty("Country")
        private String Country;

        public Inconjunctions() {
        }

        public Inconjunctions(String Country) {
            this.Country = Country;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }
    }

    public static class Image {

        @JsonProperty("URL")
        private String URL;

        public Image() {
        }

        public Image(String URL) {
            this.URL = URL;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }

    public static class Injury {

        @JsonProperty("Name")
        private String Name;

        public Injury() {
        }

        public Injury(String Name) {
            this.Name = Name;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

    }

    public static class Manufacturer {

        @JsonProperty("Name")
        private String Name;
        @JsonProperty("CompanyID")
        private String CompanyID;

        public Manufacturer() {
        }

        public Manufacturer(String Name, String CompanyID) {
            this.Name = Name;
            this.CompanyID = CompanyID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getCompanyID() {
            return CompanyID;
        }

        public void setCompanyID(String CompanyID) {
            this.CompanyID = CompanyID;
        }

    }

    public static class ManufacturerCountry {

        @JsonProperty("Country")
        private String Country;

        public ManufacturerCountry() {
        }

        public ManufacturerCountry(String Country) {
            this.Country = Country;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }

    }
    public List<String> unwindProductUPCs( ){
        List<String> unwoundProductUPCs = new ArrayList<>();
        for( ProductUPC productUPC: ProductUPCs ){
            unwoundProductUPCs.add(productUPC.getUPC());
        }
        return unwoundProductUPCs;
    }
    public List<String> unwindImages( ){
        List<String> unwoundImages = new ArrayList<>();
        for( Image image: Images ){
            unwoundImages.add(image.getURL());
        }
        return unwoundImages;
    }
    public static class ProductUPC {

        @JsonProperty("UPC")
        private String UPC;

        public ProductUPC() {
        }

        public String getUPC() {
            return UPC;
        }

        public void setUPC(String UPC) {
            this.UPC = UPC;
        }
    }

    public static class Hazard {

        public Hazard() {
        }
        @JsonProperty("Name")
        private String Name;
        @JsonProperty("HazardTypeID")
        private String HazardTypeID;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getHazardTypeID() {
            return HazardTypeID;
        }

        public void setHazardTypeID(String HazardTypeID) {
            this.HazardTypeID = HazardTypeID;
        }
    }

    public static class Remedy {

        @JsonProperty("Name")
        private String Name;

        public Remedy() {
        }

        public Remedy(String Name) {
            this.Name = Name;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }

    public static class Retailer {

        @JsonProperty("Name")
        private String Name;
        @JsonProperty("CompanyID")
        private String CompanyID;

        public Retailer() {
        }

        public Retailer(String Name, String CompanyID) {
            this.Name = Name;
            this.CompanyID = CompanyID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getCompanyID() {
            return CompanyID;
        }

        public void setCompanyID(String CompanyID) {
            this.CompanyID = CompanyID;
        }

    }

    public String getRecallID() {
        return RecallID;
    }

    public void setRecallID(String RecallID) {
        this.RecallID = RecallID;
    }

    public String getRecallNumber() {
        return RecallNumber;
    }

    public void setRecallNumber(String RecallNumber) {
        this.RecallNumber = RecallNumber;
    }

    public Date getRecallDate() {
        return RecallDate;
    }

    public void setRecallDate(Date RecallDate) {
        this.RecallDate = RecallDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getConsumerContact() {
        return ConsumerContact;
    }

    public void setConsumerContact(String ConsumerContact) {
        this.ConsumerContact = ConsumerContact;
    }

    public Date getLastPublishDate() {
        return LastPublishDate;
    }

    public void setLastPublishDate(Date LastPublishDate) {
        this.LastPublishDate = LastPublishDate;
    }

    public List<CPSCRecallProduct> getProducts() {
        return Products;
    }

    public void setProducts(List<CPSCRecallProduct> Products) {
        this.Products = Products;
    }

    public List<Inconjunctions> getInconjunctions() {
        return Inconjunctions;
    }

    public void setInconjunctions(List<Inconjunctions> Inconjunctions) {
        this.Inconjunctions = Inconjunctions;
    }

    public List<Image> getImages() {
        return Images;
    }

    public void setImages(List<Image> Images) {
        this.Images = Images;
    }

    public List<Injury> getInjuries() {
        return Injuries;
    }

    public void setInjuries(List<Injury> Injuries) {
        this.Injuries = Injuries;
    }

    public List<Manufacturer> getManufacturers() {
        return Manufacturers;
    }

    public void setManufacturers(List<Manufacturer> Manufacturers) {
        this.Manufacturers = Manufacturers;
    }

    public List<ManufacturerCountry> getManufacturerCountries() {
        return ManufacturerCountries;
    }

    public void setManufacturerCountries(List<ManufacturerCountry> ManufacturerCountries) {
        this.ManufacturerCountries = ManufacturerCountries;
    }

    public List<ProductUPC> getProductUPCs() {
        return ProductUPCs;
    }

    public void setProductUPCs(List<ProductUPC> ProductUPCs) {
        this.ProductUPCs = ProductUPCs;
    }

    public List<Hazard> getHazards() {
        return Hazards;
    }

    public void setHazards(List<Hazard> Hazards) {
        this.Hazards = Hazards;
    }

    public List<Remedy> getRemedies() {
        return Remedies;
    }

    public void setRemedies(List<Remedy> Remedies) {
        this.Remedies = Remedies;
    }

    public List<Retailer> getRetailers() {
        return Retailers;
    }

    public void setRetailers(List<Retailer> Retailers) {
        this.Retailers = Retailers;
    }

}
