package az.kerimov.fin.finance.finamance;

import java.util.List;

public class Report {
    private Integer id;
    private String name;
    private String label;
    private List<ReportParam> paramList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ReportParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ReportParam> paramList) {
        this.paramList = paramList;
    }
}
