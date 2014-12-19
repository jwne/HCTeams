package com.mongodb.util;

import org.bson.*;

class JSONParser
{
    String s;
    int pos;
    BSONCallback _callback;
    
    public JSONParser(final String s) {
        this(s, null);
    }
    
    public JSONParser(final String s, final BSONCallback callback) {
        super();
        this.pos = 0;
        this.s = s;
        this._callback = ((callback == null) ? new JSONCallback() : callback);
    }
    
    public Object parse() {
        return this.parse(null);
    }
    
    protected Object parse(final String name) {
        Object value = null;
        final char current = this.get();
        switch (current) {
            case 'n': {
                this.read('n');
                this.read('u');
                this.read('l');
                this.read('l');
                value = null;
                break;
            }
            case 'N': {
                this.read('N');
                this.read('a');
                this.read('N');
                value = Double.NaN;
                break;
            }
            case 't': {
                this.read('t');
                this.read('r');
                this.read('u');
                this.read('e');
                value = true;
                break;
            }
            case 'f': {
                this.read('f');
                this.read('a');
                this.read('l');
                this.read('s');
                this.read('e');
                value = false;
                break;
            }
            case '\"':
            case '\'': {
                value = this.parseString(true);
                break;
            }
            case '+':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                value = this.parseNumber();
                break;
            }
            case '[': {
                value = this.parseArray(name);
                break;
            }
            case '{': {
                value = this.parseObject(name);
                break;
            }
            default: {
                throw new JSONParseException(this.s, this.pos);
            }
        }
        return value;
    }
    
    public Object parseObject() {
        return this.parseObject(null);
    }
    
    protected Object parseObject(final String name) {
        if (name != null) {
            this._callback.objectStart(name);
        }
        else {
            this._callback.objectStart();
        }
        this.read('{');
        char current = this.get();
        while (this.get() != '}') {
            final String key = this.parseString(false);
            this.read(':');
            final Object value = this.parse(key);
            this.doCallback(key, value);
            if ((current = this.get()) != ',') {
                break;
            }
            this.read(',');
        }
        this.read('}');
        return this._callback.objectDone();
    }
    
    protected void doCallback(final String name, final Object value) {
        if (value == null) {
            this._callback.gotNull(name);
        }
        else if (value instanceof String) {
            this._callback.gotString(name, (String)value);
        }
        else if (value instanceof Boolean) {
            this._callback.gotBoolean(name, (boolean)value);
        }
        else if (value instanceof Integer) {
            this._callback.gotInt(name, (int)value);
        }
        else if (value instanceof Long) {
            this._callback.gotLong(name, (long)value);
        }
        else if (value instanceof Double) {
            this._callback.gotDouble(name, (double)value);
        }
    }
    
    public void read(final char ch) {
        if (!this.check(ch)) {
            throw new JSONParseException(this.s, this.pos);
        }
        ++this.pos;
    }
    
    public char read() {
        if (this.pos >= this.s.length()) {
            throw new IllegalStateException("string done");
        }
        return this.s.charAt(this.pos++);
    }
    
    public void readHex() {
        if (this.pos < this.s.length() && ((this.s.charAt(this.pos) >= '0' && this.s.charAt(this.pos) <= '9') || (this.s.charAt(this.pos) >= 'A' && this.s.charAt(this.pos) <= 'F') || (this.s.charAt(this.pos) >= 'a' && this.s.charAt(this.pos) <= 'f'))) {
            ++this.pos;
            return;
        }
        throw new JSONParseException(this.s, this.pos);
    }
    
    public boolean check(final char ch) {
        return this.get() == ch;
    }
    
    public void skipWS() {
        while (this.pos < this.s.length() && Character.isWhitespace(this.s.charAt(this.pos))) {
            ++this.pos;
        }
    }
    
    public char get() {
        this.skipWS();
        if (this.pos < this.s.length()) {
            return this.s.charAt(this.pos);
        }
        return '\uffff';
    }
    
    public String parseString(final boolean needQuote) {
        char quot = '\0';
        if (this.check('\'')) {
            quot = '\'';
        }
        else if (this.check('\"')) {
            quot = '\"';
        }
        else if (needQuote) {
            throw new JSONParseException(this.s, this.pos);
        }
        if (quot > '\0') {
            this.read(quot);
        }
        final StringBuilder buf = new StringBuilder();
        int start = this.pos;
        while (this.pos < this.s.length()) {
            final char current = this.s.charAt(this.pos);
            if (quot > '\0') {
                if (current == quot) {
                    break;
                }
            }
            else {
                if (current == ':') {
                    break;
                }
                if (current == ' ') {
                    break;
                }
            }
            if (current == '\\') {
                ++this.pos;
                final char x = this.get();
                char special = '\0';
                switch (x) {
                    case 'u': {
                        buf.append(this.s.substring(start, this.pos - 1));
                        ++this.pos;
                        final int tempPos = this.pos;
                        this.readHex();
                        this.readHex();
                        this.readHex();
                        this.readHex();
                        final int codePoint = Integer.parseInt(this.s.substring(tempPos, tempPos + 4), 16);
                        buf.append((char)codePoint);
                        start = this.pos;
                        continue;
                    }
                    case 'n': {
                        special = '\n';
                        break;
                    }
                    case 'r': {
                        special = '\r';
                        break;
                    }
                    case 't': {
                        special = '\t';
                        break;
                    }
                    case 'b': {
                        special = '\b';
                        break;
                    }
                    case '\"': {
                        special = '\"';
                        break;
                    }
                    case '\\': {
                        special = '\\';
                        break;
                    }
                }
                buf.append(this.s.substring(start, this.pos - 1));
                if (special != '\0') {
                    ++this.pos;
                    buf.append(special);
                }
                start = this.pos;
            }
            else {
                ++this.pos;
            }
        }
        buf.append(this.s.substring(start, this.pos));
        if (quot > '\0') {
            this.read(quot);
        }
        return buf.toString();
    }
    
    public Number parseNumber() {
        final char current = this.get();
        final int start = this.pos;
        boolean isDouble = false;
        if (this.check('-') || this.check('+')) {
            ++this.pos;
        }
        Label_0214: {
            while (this.pos < this.s.length()) {
                switch (this.s.charAt(this.pos)) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        ++this.pos;
                        continue;
                    }
                    case '.': {
                        isDouble = true;
                        this.parseFraction();
                        continue;
                    }
                    case 'E':
                    case 'e': {
                        isDouble = true;
                        this.parseExponent();
                        continue;
                    }
                    default: {
                        break Label_0214;
                    }
                }
            }
            try {
                if (isDouble) {
                    return Double.valueOf(this.s.substring(start, this.pos));
                }
                final Long val = Long.valueOf(this.s.substring(start, this.pos));
                if (val <= 2147483647L && val >= -2147483648L) {
                    return (int)(Object)val;
                }
                return val;
            }
            catch (NumberFormatException e) {
                throw new JSONParseException(this.s, start, e);
            }
        }
    }
    
    public void parseFraction() {
        ++this.pos;
    Label_0163:
        while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    ++this.pos;
                    continue;
                }
                case 'E':
                case 'e': {
                    this.parseExponent();
                    continue;
                }
                default: {
                    break Label_0163;
                }
            }
        }
    }
    
    public void parseExponent() {
        ++this.pos;
        if (this.check('-') || this.check('+')) {
            ++this.pos;
        }
    Label_0132:
        while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    ++this.pos;
                    continue;
                }
                default: {
                    break Label_0132;
                }
            }
        }
    }
    
    public Object parseArray() {
        return this.parseArray(null);
    }
    
    protected Object parseArray(final String name) {
        if (name != null) {
            this._callback.arrayStart(name);
        }
        else {
            this._callback.arrayStart();
        }
        this.read('[');
        int i = 0;
        char current = this.get();
        while (current != ']') {
            final String elemName = String.valueOf(i++);
            final Object elem = this.parse(elemName);
            this.doCallback(elemName, elem);
            if ((current = this.get()) == ',') {
                this.read(',');
            }
            else {
                if (current == ']') {
                    break;
                }
                throw new JSONParseException(this.s, this.pos);
            }
        }
        this.read(']');
        return this._callback.arrayDone();
    }
}
