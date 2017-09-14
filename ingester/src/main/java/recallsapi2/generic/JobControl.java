package recallsapi2.generic;

import java.util.Date;

/**
 *
 * @author ferru001
 */
public class JobControl {

    private int id;
    private String organizationName;
    private String iIngesterDefinitionName;
    private String resultsFormat;
    private int loadFrequency;
    private String rootUrl;
    private String urlParams;
    private Date lastIngestDatetime;
    private boolean active;
    private String pKeyPrefix;
    private int maxPkeyValue;
    private int maxManufacturerPkeyValue;

    public JobControl() {
    }

    public JobControl(int id, String providerName, String iIngesterDefinitionName, String resultsFormat, int loadFrequency, String rootUrl, String urlParams, Date lastIngestDatetime, boolean runIngest,
            String pKeyPrefix,
            int maxPKeyValue, int maxManufacturerPkeyValue) {
        this.id = id;
        this.organizationName = providerName;
        this.iIngesterDefinitionName = iIngesterDefinitionName;
        this.resultsFormat = resultsFormat;
        this.loadFrequency = loadFrequency;
        this.rootUrl = rootUrl;
        this.urlParams = urlParams;
        this.lastIngestDatetime = lastIngestDatetime;
        this.active = runIngest;
        this.pKeyPrefix = pKeyPrefix;
        this.maxPkeyValue = maxPKeyValue;
        this.maxManufacturerPkeyValue=maxManufacturerPkeyValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getiIngesterDefinitionName() {
        return iIngesterDefinitionName;
    }

    public void setiIngesterDefinitionName(String iIngesterDefinitionName) {
        this.iIngesterDefinitionName = iIngesterDefinitionName;
    }

    public String getResultsFormat() {
        return resultsFormat;
    }

    public void setResultsFormat(String resultsFormat) {
        this.resultsFormat = resultsFormat;
    }

    public int getLoadFrequency() {
        return loadFrequency;
    }

    public void setLoadFrequency(int loadFrequency) {
        this.loadFrequency = loadFrequency;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public Date getLastIngestDatetime() {
        return lastIngestDatetime;
    }

    public void setLastIngestDatetime(Date lastIngestDatetime) {
        this.lastIngestDatetime = lastIngestDatetime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getpKeyPrefix() {
        return pKeyPrefix;
    }

    public void setpKeyPrefix(String pKeyPrefix) {
        this.pKeyPrefix = pKeyPrefix;
    }

    public int getMaxPkeyValue() {
        return maxPkeyValue;
    }

    public void setMaxPkeyValue(int maxPkeyValue) {
        this.maxPkeyValue = maxPkeyValue;
    }

    public int getMaxManufacturerPkeyValue() {
        return maxManufacturerPkeyValue;
    }

    public void setMaxManufacturerPkeyValue(int maxManufacturerPkeyValue) {
        this.maxManufacturerPkeyValue = maxManufacturerPkeyValue;
    }

}
