package Model;

public class Book {
    private int id, cover, year;
    private String title, serie, autor;
    private static int autoIncrementId = 1;

    public Book(int id,int cover, int year, String title, String serie, String autor) {
        this.id = id;
        this.cover = cover;
        this.year = year;
        this.title = title;
        this.serie = serie;
        this.autor = autor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
