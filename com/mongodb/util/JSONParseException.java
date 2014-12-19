package com.mongodb.util;

public class JSONParseException extends RuntimeException
{
    private static final long serialVersionUID = -4415279469780082174L;
    String s;
    int pos;
    
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(this.s);
        sb.append("\n");
        for (int i = 0; i < this.pos; ++i) {
            sb.append(" ");
        }
        sb.append("^");
        return sb.toString();
    }
    
    public JSONParseException(final String s, final int pos) {
        super();
        this.s = s;
        this.pos = pos;
    }
    
    public JSONParseException(final String s, final int pos, final Throwable cause) {
        super(cause);
        this.s = s;
        this.pos = pos;
    }
}
