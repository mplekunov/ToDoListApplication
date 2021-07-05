package ucf.assignments;

public class Database {
    private java.util.List<List> listCollection;
    private File file;

    public Database(File file) {
        this.file = file;
        download();
    }

    private void download() {
        //gets content from file
        //Deserializes content into collection
        //assigns collection to listCollection
        listCollection = Deserializer.deserialize(file.readAll());
        throw new UnsupportedOperationException();
    }

    public void upload() {
        //gets collection of lists
        //Serialize collection
        //upload serialized content into file
        file.write(Serializer.serialize(listCollection));
        throw new UnsupportedOperationException();
    }

    public java.util.List<List> getListCollection() {
        return listCollection;
    }

    public void setListCollection(java.util.List<List> listCollection) {
        this.listCollection = listCollection;
    }
}
