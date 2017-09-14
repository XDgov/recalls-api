package recallsapi2.ingester.dao;

import com.jolbox.bonecp.BoneCP;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import recallsapi2.dto.JobControl;
import recallsapi2.ingester.dto.cpsc.CPSCRecallsItem;
import recallsapi2.recallsglobals.BasicJdbcSupport;
import recallsapi2.recallsglobals.GlobalDataAccess;
import recallsapi2.utils.GeneralUtils;

/**
 *
 * @author ferru001
 */
public class CPSCIngesterDAO extends GlobalDataAccess {

    private static final Logger logger = Logger.getLogger(CPSCIngesterDAO.class);

    public CPSCIngesterDAO(BoneCP connectionPool) throws IOException, SQLException {
        super(connectionPool);
    }

    public void loadCPSCRecallsRecords(List<CPSCRecallsItem> cpscRecallItems, JobControl jc) {
        Connection conn = null;
        PreparedStatement recallsPStmt = null;
        PreparedStatement productsPStmt = null;
        PreparedStatement manufacturerPStmt = null;
        PreparedStatement manufacturerLocationsPStmt = null;
        PreparedStatement hazardsPStmt = null;
        PreparedStatement retailersPStmt = null;

        boolean success = true;
        try {
            conn = getConnectionPool().getConnection();
            conn.setAutoCommit(false);

            //create the prepared statements 
            recallsPStmt = conn.prepareStatement(INSERT_RECALLS_SQL);

            //get the current max PKey for CPSC recalls
            int currMaxPKeyVal = jc.getMaxPkeyValue();
            for (CPSCRecallsItem currRecallItem : cpscRecallItems) {
                recallsPStmt.setString(1, jc.getpKeyPrefix() + "-" + (++currMaxPKeyVal));//ID
                recallsPStmt.setInt(2, jc.getId());//SOURCE_ORGANIZATION
                recallsPStmt.setString(3, currRecallItem.getRecallID());//RECALLS_ID
                recallsPStmt.setString(4, currRecallItem.getRecallNumber());//RECALL_NUMBER
                recallsPStmt.setString(5, currRecallItem.getTitle());//RECALL_REASON
                recallsPStmt.setString(6, null);//RECALL_STATUS
                recallsPStmt.setDate(7, currRecallItem.getRecallDate() == null ? null : new java.sql.Date(currRecallItem.getRecallDate().getTime()));//RECALL_DATE
                recallsPStmt.setDate(8, currRecallItem.getLastPublishDate() == null ? null : new java.sql.Date(currRecallItem.getLastPublishDate().getTime()));//LAST_PUBLISH_DATE
                recallsPStmt.setString(9, currRecallItem.getDescription());//RECALL_DESCRIPTION
                recallsPStmt.setString(10, currRecallItem.getURL());//RECALL_URL
                recallsPStmt.setString(11, null);//RECALL_CLASSIFICATION
                recallsPStmt.setString(12, null);//RECALL_VOLUNTARY_MANDATED
                recallsPStmt.setString(13, currRecallItem.getConsumerContact());//CONSUMER_CONTACT
                recallsPStmt.setString(14, GeneralUtils.concat(currRecallItem.unwindInjuries(), ">::<"));//REPORTED_COMPLAINTS
                recallsPStmt.setString(15, GeneralUtils.concat(currRecallItem.unwindRemedies(), ">::<"));//REMEDIES
                int insertedRec = recallsPStmt.executeUpdate();
                if (insertedRec != 1) {
                    String errMsg = String.format("Failure to insert RECALLS item, inserted %d, but expected to insert 1", insertedRec);
                    logger.error(errMsg);
                    throw new SQLException(errMsg);
                }

                productsPStmt = conn.prepareStatement(INSERT_PRODUCTS_SQL);
                //we inserted the RECALLS record successfully - let's move on to the PRODUCTS
                for (CPSCRecallsItem.CPSCRecallProduct product : currRecallItem.getProducts()) {
//                    RECALLS_ID,UPCS,NAME,DESCRIPTION,MODEL,TYPE,CATEGORY_ID,IMAGES,PRODUCT_QUANTITY
                    productsPStmt.setString(1, jc.getpKeyPrefix() + "-" + (currMaxPKeyVal));//RECALLS_ID
                    productsPStmt.setString(2, GeneralUtils.concat(currRecallItem.unwindProductUPCs(), ">::<"));//UPCS
                    productsPStmt.setString(3, product.getName());//NAME
                    productsPStmt.setString(4, product.getDescription());//DESCRIPTION
                    productsPStmt.setString(5, product.getModel());//MODEL
                    productsPStmt.setString(6, product.getType());//TYPE
                    productsPStmt.setString(7, product.getCategoryID());//CATEGORY_ID
                    productsPStmt.setString(8, GeneralUtils.concat(currRecallItem.unwindImages(), ">::<"));//IMAGES
                    productsPStmt.setString(9, product.getNumberOfUnits());//PRODUCT_QUANTITY
                    insertedRec = productsPStmt.executeUpdate();
                    if (insertedRec != 1) {
                        String errMsg = String.format("Failure to insert PRODUCTS item, inserted %d, but expected to insert 1", insertedRec);
                        logger.error(errMsg);
                        throw new SQLException(errMsg);
                    }
                }
                manufacturerPStmt = conn.prepareStatement(INSERT_MANUFACTURER_SQL);

                manufacturerLocationsPStmt = conn.prepareStatement(INSERT_MANUFACTURER_LOCATIONS_SQL);
                //we inserted PRODUCTS correctly - now move on to MANUFACTURERS
                for (CPSCRecallsItem.Manufacturer manufacturer : currRecallItem.getManufacturers()) {
                    String manufacturerId = jc.getpKeyPrefix() + "-" + (jc.getMaxManufacturerPkeyValue());
                    manufacturerPStmt.setString(1, manufacturerId);//ID
                    manufacturerPStmt.setString(2, jc.getpKeyPrefix() + "-" + (currMaxPKeyVal));//RECALLS_ID
                    manufacturerPStmt.setString(3, manufacturer.getName());//NAME
                    manufacturerPStmt.setString(4, manufacturer.getCompanyID());//COMPANY_ID
                    insertedRec = manufacturerPStmt.executeUpdate();
                    if (insertedRec != 1) {
                        String errMsg = String.format("Failure to insert MANUFACTURER item, inserted %d, but expected to insert 1", insertedRec);
                        logger.error(errMsg);
                        throw new SQLException(errMsg);
                    }
                    //we inserted MANUFACTURER correctly - now move on to MANUFACTURERS_LOCATIONS
                    for (CPSCRecallsItem.ManufacturerCountry manufacturerCountry : currRecallItem.getManufacturerCountries()) {
                        manufacturerLocationsPStmt.setString(1, manufacturerId);//MANUFACTURER_ID
                        manufacturerLocationsPStmt.setString(2, manufacturerCountry.getCountry());//COUNTRY
                        manufacturerLocationsPStmt.setString(3, null);//STATE
                        manufacturerLocationsPStmt.setString(4, null);//CITY
                        insertedRec = manufacturerLocationsPStmt.executeUpdate();
                        if (insertedRec != 1) {
                            String errMsg = String.format("Failure to insert MANUFACTURER_LOCATIONS item, inserted %d, but expected to insert 1", insertedRec);
                            logger.error(errMsg);
                            throw new SQLException(errMsg);
                        }
                    }
                }

                hazardsPStmt = conn.prepareStatement(INSERT_HAZARDS_SQL);
                //Move on to Hazards
                for (CPSCRecallsItem.Hazard hazard : currRecallItem.getHazards()) {
                    hazardsPStmt.setString(1, jc.getpKeyPrefix() + "-" + (currMaxPKeyVal));
                    hazardsPStmt.setString(2, hazard.getName());
                    hazardsPStmt.setString(3, hazard.getHazardTypeID());
                    insertedRec = hazardsPStmt.executeUpdate();
                    if (insertedRec != 1) {
                        String errMsg = String.format("Failure to insert HAZARDS item, inserted %d, but expected to insert 1", insertedRec);
                        logger.error(errMsg);
                        throw new SQLException(errMsg);
                    }
                }
                
                retailersPStmt = conn.prepareStatement(INSERT_RETAILERS_SQL);
                //done inserting HAZARDS - moving on to RETAILERS
                //RECALLS_ID,NAME,COMPANY_ID    
                 for (CPSCRecallsItem.Retailer retailer : currRecallItem.getRetailers()) {
                    retailersPStmt.setString(1, jc.getpKeyPrefix() + "-" + (currMaxPKeyVal));//RECALLS_ID
                    retailersPStmt.setString(2, retailer.getName());//NAME
                    retailersPStmt.setString(3, retailer.getCompanyID());//COMPANY_ID
                    insertedRec = retailersPStmt.executeUpdate();
                    if (insertedRec != 1) {
                        String errMsg = String.format("Failure to insert RETAILERS item, inserted %d, but expected to insert 1", insertedRec);
                        logger.error(errMsg);
                        throw new SQLException(errMsg);
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            success = false;
        } finally {
            BasicJdbcSupport.close(productsPStmt);
            BasicJdbcSupport.close(manufacturerPStmt);
            BasicJdbcSupport.close(manufacturerLocationsPStmt);
            BasicJdbcSupport.close(hazardsPStmt);
            BasicJdbcSupport.close(retailersPStmt);
            BasicJdbcSupport.close(true, success, conn, recallsPStmt, null);
        }
    }

}
