package DataModel;

public class Show {
    public String name;
    public String date;
    public String hostUsername;
    public String locatedIn;

    public Show(String name, String date, String hostUsername, String locatedIn) {
        this.name = name;
        this.date = date;
        this.hostUsername = hostUsername;
        this.locatedIn = locatedIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public String getLocatedIn() {
        return locatedIn;
    }

    public void setLocatedIn(String locatedIn) {
        this.locatedIn = locatedIn;
    }
}
