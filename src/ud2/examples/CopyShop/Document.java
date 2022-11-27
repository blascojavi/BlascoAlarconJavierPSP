package ud2.examples.CopyShop;

public class Document {
    private String title;
    private int pages;

    public Document(String name, int pages) {
        this.title = name;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }
}