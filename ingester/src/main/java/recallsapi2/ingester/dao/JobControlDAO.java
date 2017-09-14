package recallsapi2.ingester.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import recallsapi2.dto.JobControl;
import recallsapi2.recallsglobals.GlobalDataAccess;

/**
 *
 * @author ferru001
 */
public class JobControlDAO extends GlobalDataAccess {

    private static final Logger logger = Logger.getLogger(JobControlDAO.class);

    public JobControlDAO(String propertyFileName) throws IOException, SQLException {
        super(propertyFileName);
    }

    public List<JobControl> getJobControls(boolean getOnlyActive) {
        List<JobControl> jobControlList = new ArrayList<>();
        String sql = "SELECT * FROM JOB_CONTROL" + (getOnlyActive ? " WHERE ACTIVE=TRUE" : "");
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while( rs.next() ){
                jobControlList.add(getJobControl(rs));
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            close(rs, pStmt, conn);
        }
        return jobControlList;
    }

    public JobControl getJobControl(ResultSet rs) throws SQLException {
        JobControl jc = new JobControl();
        jc.setId(rs.getInt("ID"));
        jc.setActive(rs.getBoolean("ACTIVE"));
        jc.setLastIngestDatetime(rs.getDate("LAST_INGEST_DATETIME"));
        jc.setLoadFrequency(rs.getInt("LOAD_FREQUENCY"));
        jc.setProviderName(rs.getString("PROVIDER_NAME"));
        jc.setResultsFormat(rs.getString("RESULTS_FORMAT"));
        jc.setRootUrl(rs.getString("ROOT_URL"));
        jc.setUrlParams(rs.getString("URL_PARAMS"));
        jc.setiIngesterDefinitionName(rs.getString("IINGESTER_DEFINITION_NAME"));
        jc.setpKeyPrefix(rs.getString("RECALLS_ID_PREFIX"));
        jc.setMaxPkeyValue(rs.getInt("MAX_PKEY_VALUE"));
        jc.setMaxManufacturerPkeyValue(rs.getInt("MAX_MANUFACTURER_PKEY_VALUE"));
        return jc;
    }
}
