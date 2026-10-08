
public enum Color {
    RED,
    BLACK;
    // looks nicer than char
    public Color opponent() {
        return this == RED ? BLACK : RED; 
    }
}