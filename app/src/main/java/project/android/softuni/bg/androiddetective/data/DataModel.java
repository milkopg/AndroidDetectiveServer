package project.android.softuni.bg.androiddetective.data;

/**
 * Created by Milko G on 22/09/2016.
 */
public class DataModel {

    private int icon;
    private String name;
    private String receiverName;

  /**
   * DataModel for constructing menus in Drawer
   * @param icon - icon of menu
   * @param name - name of menu
   * @param receiverName - related receiver name in menu
   */
    public DataModel(int icon, String name, String receiverName) {

        this.icon = icon;
        this.name = name;
        this.receiverName = receiverName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
