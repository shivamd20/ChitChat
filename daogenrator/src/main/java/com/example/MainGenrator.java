package com.example;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class MainGenrator{

    public static void main(String[] args)  throws Exception {

        //place where db folder will be created inside the project folder
        Schema schema = new Schema(1,"io.hasura.shivam.chitchat.db");

        addTables(schema);
        //  ./app/src/main/java/   ----   com/codekrypt/greendao/db is the full path
        new DaoGenerator().generateAll(schema, "./app/src/main/java");

    }

    public static void addTables(Schema schema)
    {
        Entity person=schema.addEntity("person");

        person.addStringProperty("mobile").notNull().primaryKey()
        ;
        person.addByteArrayProperty("profile_pic");
        Entity convers=schema.addEntity("conversation");



       Property with= convers.addStringProperty("with").notNull().getProperty();

        convers.addToOne(person,with);
        convers.addStringProperty("message").notNull();
        convers.addBooleanProperty("isDraw").notNull();
        convers.addBooleanProperty("isDelivered").notNull();
        convers.addBooleanProperty("isMe").notNull();
        convers.addDateProperty("datetime").primaryKey();

    }
}
