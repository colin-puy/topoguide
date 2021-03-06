package fr.colin.topoguide.database;

import static fr.colin.topoguide.database.table.SommetTable.TABLE_NAME;

import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import fr.colin.topoguide.database.table.DepartTable;
import fr.colin.topoguide.database.table.ItineraireTable;
import fr.colin.topoguide.database.table.TopoGuideTable;
import fr.colin.topoguide.utils.IOUtils;
import fr.colin.topoguide.views.R;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

   private static final String BASE_NAME = "topoguide.db";
   private static final int CURRENT_BASE_VERSION = 3;
   
   private final Context context;

   public DatabaseOpenHelper(Context context) {
      super(context, BASE_NAME, null, CURRENT_BASE_VERSION);
      this.context = context;
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(getDbRequestFromRawResources(R.raw.create_table_topoguide));
      db.execSQL(getDbRequestFromRawResources(R.raw.create_table_itineraire));
      db.execSQL(getDbRequestFromRawResources(R.raw.create_table_sommet));
      db.execSQL(getDbRequestFromRawResources(R.raw.create_table_depart));
   }

   // TODO supprimer le bout de code
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE " + ItineraireTable.TABLE + ";");
      db.execSQL("DROP TABLE " + TopoGuideTable.TABLE_NAME + ";");
      db.execSQL("DROP TABLE " + TABLE_NAME + ";");
      db.execSQL("DROP TABLE " + DepartTable.TABLE + ";");
      onCreate(db);
   }

   private String getDbRequestFromRawResources(int id) {
      InputStream openRawResource = context.getResources().openRawResource(id);
      return IOUtils.inputStreamToString(openRawResource);
   }
}
