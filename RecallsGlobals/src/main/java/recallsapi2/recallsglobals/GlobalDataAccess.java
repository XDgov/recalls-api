package recallsapi2.recallsglobals;

import com.jolbox.bonecp.BoneCP;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import recallsapi2.utils.GeneralUtils;

/**
 *
 * @author ferru001
 */
public class GlobalDataAccess extends BasicJdbcSupport {

    private static final Logger logger = Logger.getLogger(GlobalDataAccess.class);
    //private DataSource ds;
    private BoneCP connectionPool = null;

    protected String RECALLS_COLS = "ID,SOURCE_ORGANIZATION,RECALL_ID,RECALL_NUMBER,RECALL_REASON,RECALL_STATUS,RECALL_DATE,LAST_PUBLISH_DATE,RECALL_DESCRIPTION,RECALL_URL,RECALL_CLASSIFICATION,RECALL_VOLUNTARY_MANDATED,CONSUMER_CONTACT,REPORTED_COMPLAINTS,REMEDIES";
    protected String PRODUCTS_COLS = "RECALLS_ID,UPCS,NAME,DESCRIPTION,MODEL,TYPE,CATEGORY_ID,IMAGES,PRODUCT_QUANTITY";
    protected String MANUFACTURER_COLS = "ID,RECALLS_ID,NAME,COMPANY_ID";
    protected String MANUFACTURER_LOCATIONS_COLS = "MANUFACTURERS_ID,COUNTRY,STATE,CITY";
    protected String HAZARDS_COLS = "RECALLS_ID,HAZARD,HAZARD_TYPE_ID";
    protected String RETAILERS_COLS = "RECALLS_ID,NAME,COMPANY_ID";

    protected String INSERT_RECALLS_SQL = String.format("INSERT INTO RECALLS (%s) VALUES (%s)", RECALLS_COLS, GeneralUtils.multiplyStringWithToken("?", ",", RECALLS_COLS.split(",").length));
    protected String INSERT_PRODUCTS_SQL = String.format("INSERT INTO PRODUCTS (%s) VALUES (%s)", PRODUCTS_COLS, GeneralUtils.multiplyStringWithToken("?", ",", PRODUCTS_COLS.split(",").length));
    protected String INSERT_MANUFACTURER_SQL = String.format("INSERT INTO MANUFACTURERS (%s) VALUES (%s)", MANUFACTURER_COLS, GeneralUtils.multiplyStringWithToken("?", ",", MANUFACTURER_COLS.split(",").length));
    protected String INSERT_MANUFACTURER_LOCATIONS_SQL = String.format("INSERT INTO MANUFACTURERS_LOCATIONS (%s) VALUES (%s)", MANUFACTURER_LOCATIONS_COLS, GeneralUtils.multiplyStringWithToken("?", ",", MANUFACTURER_LOCATIONS_COLS.split(",").length));
    protected String INSERT_HAZARDS_SQL = String.format("INSERT INTO HAZARDS (%s) VALUES (%s)", HAZARDS_COLS, GeneralUtils.multiplyStringWithToken("?", ",", HAZARDS_COLS.split(",").length));
    protected String INSERT_RETAILERS_SQL = String.format("INSERT INTO RETAILERS (%s) VALUES (%s)", RETAILERS_COLS, GeneralUtils.multiplyStringWithToken("?", ",", RETAILERS_COLS.split(",").length));

    public GlobalDataAccess(BoneCP connectionPool) {
        this.connectionPool = connectionPool;
    }

    public GlobalDataAccess(String propertyFileName) throws IOException, SQLException {

        connectionPool = createConnectionPool(propertyFileName);
    }

    public BoneCP getConnectionPool() {
        return connectionPool;
    }

    public Connection getConn() throws SQLException {
        Connection conn = null;
        try {
            long start = System.currentTimeMillis();
            conn = getConnectionPool().getConnection();
            long end = System.currentTimeMillis();
            logger.debug("Read/Write Conn retrieved in " + (end - start) + " ms");
        } catch (Exception ex) {
            throw ex;
        }
        return conn;
    }
}
