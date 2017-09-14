package recallsapi2.recallsglobals;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Provides some basic JDBC support methods.
 *
 * @author ferru001
 */
public class BasicJdbcSupport {

    private static final Logger logger = Logger.getLogger(BasicJdbcSupport.class);

    /**
     * Closes the given JDBC resources.
     *
     * @param rs ResultSet resource.
     * @param stmt Statement resource.
     * @param conn Connection resource.
     *
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        BasicJdbcSupport.close(rs);
        BasicJdbcSupport.close(stmt);
        BasicJdbcSupport.close(conn);
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    public static void close(Statement stmt) {
        //close the statement
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    public static void close(Connection conn) {
        //close the connection
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    public static void close(boolean trans, boolean success, Connection conn, Statement stmt, ResultSet rs) {
        if (trans) {
            if (success) {
                try {
                    if (conn != null) {
                        conn.commit();
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            } else {
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        close(rs, stmt, conn);
    }

    public static void close(boolean trans, boolean success, Connection conn, Statement stmt, ResultSet rs, boolean closeConnection, boolean closeStatement) {
        if (trans) {
            if (success) {
                try {
                    if (conn != null) {
                        conn.commit();
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            } else {
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        close(rs, closeStatement == false ? null : stmt, closeConnection == false ? null : conn);
    }

    /**
     * Evaluate the the integer array that is returned by
     * Statement.executeBatch.
     *
     * @param batchInserts int array returned by executeBatch method
     * @param expNumRecs the expected number of records that were inserted in
     * the batch statement.
     * @return true/false whether the batch return evaluates as successful.
     */
    public static boolean evaluateBatchReturn(int[] batchInserts, int expNumRecs) {
        boolean success = true;
        if (batchInserts == null || batchInserts.length != expNumRecs) {
            success = false;
        } else {
            for (int insert : batchInserts) {
                if (insert == Statement.EXECUTE_FAILED) {
                    success = false;
                    break;
                }
            }
        }
        return success;
    }

    public static int getNumSuccessInserted(int[] batchInserts) {
        int totalSuccessInsert = 0;
        for (int insert : batchInserts) {
            if (insert != Statement.EXECUTE_FAILED) {
                totalSuccessInsert++;
            }
        }
        return totalSuccessInsert;
    }

    public static void rollbackConns(Connection[] conns) {
        if (conns != null) {
            for (Connection conn : conns) {
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    public static void commitConns(Connection[] conns) {
        if (conns != null) {
            for (Connection conn : conns) {
                try {
                    if (conn != null) {
                        conn.commit();
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    public static void commitOrRollbackConns(boolean success, Connection[] conns) {
        if (success) {
            commitConns(conns);
        } else {
            rollbackConns(conns);
        }
    }

    public static BoneCP createConnectionPool(String propertyFileName) throws IOException, SQLException {
        BoneCP connectionPool = null;
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName));

        //setup the connection pool
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl(props.getProperty("dburl")); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
        config.setUsername(props.getProperty("dbusername"));
        config.setPassword(props.getProperty("dbpassword"));
        config.setMinConnectionsPerPartition(5);
        config.setMaxConnectionsPerPartition(10);
        config.setLogStatementsEnabled(true);
        config.setPartitionCount(1);
        connectionPool = new BoneCP(config);
        return connectionPool;
    }
}
