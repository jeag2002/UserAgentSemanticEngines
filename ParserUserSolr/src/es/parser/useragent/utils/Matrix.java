package es.parser.useragent.utils;

class Matrix {
    String[][] data;
    int x, y, columns, rows;

    public Matrix(String[][] data) {
        this(data, 0, 0, data.length, data[0].length);
    }

    private Matrix(String[][] data, int x, int y, int columns, int rows) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.columns = columns;
        this.rows = rows;
    }

    public Matrix getSubMatrix(int x, int y, int columns, int rows) {
        return new Matrix(data, this.x + x , this.y + y, columns, rows);
    }

    public String[][] getData(){
    	
    	String subData[][] = new String[rows][columns];
    	
        for (int i = x; i < x + rows; i++) {
            for (int j = y; j < y + columns; j++) {
            	subData[i-x][j-y] = data[i][j];
            }
        }
        
    	return subData;
    	
    }
    
    
    /*
    public String toString() {

        StringBuffer sb = new StringBuffer();

        for (int i = y; i < x + rows; i++) {
            for (int j = x; j < x + columns; j++)
                sb.append(data[i][j]).append(" ");

            sb.append("\n");
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }
    */
}