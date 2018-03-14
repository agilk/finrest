package az.kerimov.fin.finance.finamance;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "fn_logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "log_type")
    private String logType;

    @Column(name = "parent_log_id")
    private Integer parentLog;

    @Column(name = "log_date")
    private Date logDate;

    @Column(name = "log_day")
    private String logDay;

    @Column(name = "proc_name")
    private String procName;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "log_string")
    private String logString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Integer getParentLog() {
        return parentLog;
    }

    public void setParentLog(Integer parentLog) {
        this.parentLog = parentLog;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getLogDay() {
        return logDay;
    }

    public void setLogDay(String logDay) {
        this.logDay = logDay;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getLogString() {
        return logString;
    }

    public void setLogString(String logString) {
        this.logString = logString;
    }
}

