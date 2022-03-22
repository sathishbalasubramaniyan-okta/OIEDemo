package com.okta.demo.util;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormatter{

    public static String format(final JSONObject object) throws JSONException{
        final JsonVisitor visitor = new JsonVisitor(4, ' ');
        visitor.visit(object, 0);
        return visitor.toString();
    }

    private static class JsonVisitor{

        private final StringBuilder builder = new StringBuilder();
        private final int indentationSize;
        private final char indentationChar;

        public JsonVisitor(final int indentationSize, final char indentationChar){
            this.indentationSize = indentationSize;
            this.indentationChar = indentationChar;
        }

        private void visit(final JSONArray array, final int indent) throws JSONException{
            final int length = array.length();
            if(length == 0){
                write("[]", indent);
            } else{
                write("[", 1);
                for(int i = 0; i < length; i++){
                    visit(array.get(i), indent + 1);
                }
                writeNoReturn("]", 1);
            }

        }

        private void visit(final JSONObject obj, final int indent) throws JSONException{
            final int length = obj.length();
            if(length == 0){
                write("{}", indent);
            } else{
                write("{", 0);
                final Iterator<String> keys = obj.keys();
                while(keys.hasNext()){
                    final String key = keys.next();

                    writeNoReturn(key + " :", indent + 1);
                    visitNoReturn(obj.get(key), indent + 1);
                    if(keys.hasNext()){
                    	write(",", 0);
                    }
                    else {
                    	write("", 0);
                    }
                }
                writeNoReturn("}", 0);
            }

        }

        private void visit(final Object object, final int indent) throws JSONException{
            if(object instanceof JSONArray){
                visit((JSONArray) object, indent);
            } else if(object instanceof JSONObject){
                visit((JSONObject) object, indent);
            } else{
                if(object instanceof String){
                	write("\"" + (String) object + "\"", indent);
                } else{
                	write(String.valueOf(object), indent);
                }
            }

        }
        
        private void visitNoReturn(final Object object, final int indent) throws JSONException{
            if(object instanceof JSONArray){
                visit((JSONArray) object, indent);
            } else if(object instanceof JSONObject){
                visit((JSONObject) object, indent);
            } else{
                if(object instanceof String){
                	writeNoReturn("\"" + (String) object + "\"", indent);
                } else{
                	writeNoReturn(String.valueOf(object), indent);
                }
            }

        }

        private void write(final String data, final int indent){
            for(int i = 0; i < (indent * indentationSize); i++){
                builder.append(indentationChar);
            }
            builder.append(data).append("\n");
        }
        
        private void writeNoReturn(final String data, final int indent){
            for(int i = 0; i < (indent * indentationSize); i++){
                builder.append(indentationChar);
            }
            builder.append(data);
        }

        @Override
        public String toString(){
            return builder.toString();
        }

    }
    
    public static void main(final String[] args) throws JSONException{
        final JSONObject obj =
                new JSONObject("{\"glossary\":{\"title\": \"example glossary\", \"GlossDiv\":{\"title\": \"S\", \"GlossList\":{\"GlossEntry\":{\"ID\": \"SGML\", \"SortAs\": \"SGML\", \"GlossTerm\": \"Standard Generalized Markup Language\", \"Acronym\": \"SGML\", \"Abbrev\": \"ISO 8879:1986\", \"GlossDef\":{\"para\": \"A meta-markup language, used to create markup languages such as DocBook.\", \"GlossSeeAlso\": [\"GML\", \"XML\"]}, \"GlossSee\": \"markup\"}}}}}");
        System.out.println(JsonFormatter.format(obj));
    }   

}

