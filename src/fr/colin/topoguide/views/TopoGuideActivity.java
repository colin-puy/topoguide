package fr.colin.topoguide.views;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import fr.colin.topoguide.model.TopoGuide;
import fr.colin.topoguide.repository.ImageRepository;
import fr.colin.topoguide.repository.LocalTopoGuideRepository;
import fr.colin.topoguide.views.adapter.TopoGuideListAdapter;

/**
 * TODO : - mise en cache de la liste des topos - clean code
 * 
 * @author colin
 * 
 */
public class TopoGuideActivity extends ListActivity {
   private static final int ACTIVITY_EDIT = 1;

   private static final int INSERT_ID = Menu.FIRST;
   private static final int DELETE_ID = Menu.FIRST + 1;

   private LocalTopoGuideRepository topoguideRepository;

   private ImageRepository imageRepository;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.topo_list);
      topoguideRepository = LocalTopoGuideRepository.fromContext(this);
      topoguideRepository.open();
      imageRepository = new ImageRepository(this);
      fillData();
      registerForContextMenu(getListView());
   }

   private void fillData() {
      setListAdapter(new TopoGuideListAdapter(this, topoguideRepository.fetchAllTopoListItems()));
   }

   @Override
   protected void onPause() {
      topoguideRepository.close();
      super.onPause();
   }

   @Override
   protected void onResume() {
      topoguideRepository.open();
      super.onResume();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      boolean result = super.onCreateOptionsMenu(menu);
      menu.add(0, INSERT_ID, 0, R.string.menu_download);
      return result;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case INSERT_ID:
         downloadTopo();
         return true;
      }
      return super.onOptionsItemSelected(item);
   }

   private void downloadTopo() {
      startActivityForResult(new Intent(this, Download.class), ACTIVITY_EDIT);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
      super.onActivityResult(requestCode, resultCode, intent);

      if (resultCode == RESULT_OK) {
         TopoGuide topo = (TopoGuide) intent.getExtras().getParcelable("downloaded_topo");
         topoguideRepository.open();
         topo = topoguideRepository.create(topo);
         try {
            imageRepository.addImagesForTopo(topo);
         } catch (IOException e) {
            // TODO
            e.printStackTrace();
         }
         fillData();
      }
   }

   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      menu.add(0, DELETE_ID, 0, R.string.menu_delete);
   }

   @Override
   public boolean onContextItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case DELETE_ID:
         AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
         // topoguideRepository.delete(info.id);
         fillData();
         return true;
      }
      return super.onContextItemSelected(item);
   }

   @Override
   protected void onListItemClick(ListView l, View v, int position, long id) {
      super.onListItemClick(l, v, position, id);
      Intent i = new Intent(this, TopoGuideDetailsTab.class);
      i.putExtra("current_topo", topoguideRepository.findTopoById(id));
      startActivity(i);
   }
}
