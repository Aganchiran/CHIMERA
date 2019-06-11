package com.aganchiran.chimera.chimerafront.activities;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimerafront.utils.listeners.AbstractDropToListener;
import com.aganchiran.chimera.chimerafront.utils.views.EventPoint;
import com.aganchiran.chimera.chimerafront.utils.views.ZoomLayout;
import com.aganchiran.chimera.viewmodels.EventMapVM;

import java.io.File;
import java.util.List;

public class EventMapActivity extends ActivityWithUpperBar {

    private static final int REQUEST_STORAGE = 112;
    private static final int PICK_IMAGE = 1;
    private EventMapVM eventMapVM;
    private CampaignModel campaign;
    private ZoomLayout zoomLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_map);
        eventMapVM = ViewModelProviders.of(this).get(EventMapVM.class);
        zoomLayout = findViewById(R.id.zoom_layout);
        final ImageView deleteArea = findViewById(R.id.delete_area);

        zoomLayout.setListener(new ZoomLayout.OnItemClickListener() {
            @Override
            public void onCreateEvent(final EventPoint eventPoint) {
                eventMapVM.insert(new EventModel(
                        "New Event",
                        "",
                        eventPoint.getXCoord(),
                        eventPoint.getYCoord(),
                        campaign.getId()));
                eventMapVM.getEventByCoordsAndCampaign(eventPoint.getXCoord(), eventPoint.getYCoord(),
                        campaign.getId()).observe(EventMapActivity.this, new Observer<EventModel>() {
                    @Override
                    public void onChanged(@Nullable EventModel eventModel) {
                        if (eventModel != null) {
                            eventPoint.setId(eventModel.getId());
                        }
                    }
                });
            }

            @Override
            public void onDeleteEvent(EventPoint eventPoint) {
                final EventModel eventModel = new EventModel(
                        "",
                        "",
                        eventPoint.getXCoord(),
                        eventPoint.getYCoord(),
                        campaign.getId());
                eventModel.setId(eventPoint.getId());
                eventMapVM.delete(eventModel);
            }

            @Override
            public void onMoveEvent(final EventPoint eventPoint) {
                eventMapVM.getEventById(eventPoint.getId()).observe(EventMapActivity.this, new Observer<EventModel>() {
                    @Override
                    public void onChanged(@Nullable EventModel eventModel) {
                        if (eventModel != null) {
                            eventModel.setXCoord(eventPoint.getXCoord());
                            eventModel.setYCoord(eventPoint.getYCoord());
                            eventMapVM.update(eventModel);
                        }
                    }
                });

            }

            @Override
            public void onEventClick(EventPoint eventPoint) {
                final LiveData<EventModel> eventLiveData = eventMapVM.getEventById(eventPoint.getId());
                eventLiveData.observe(EventMapActivity.this, new Observer<EventModel>() {
                    @Override
                    public void onChanged(@Nullable EventModel eventModel) {
                        eventLiveData.removeObserver(this);
                        Intent intent = new Intent(EventMapActivity.this,
                                EventProfileActivity.class);
                        intent.putExtra("EVENT", eventModel);
                        startActivity(intent);
                    }
                });
            }
        });

        campaign = (CampaignModel) getIntent().getSerializableExtra("CAMPAIGN");
        if (campaign.getBackgroundImage() != null) {
            zoomLayout.setBackgroundImage(Uri.parse(campaign.getBackgroundImage()));
        }
        eventMapVM.getCampaignEvents(campaign.getId()).observe(this, new Observer<List<EventModel>>() {
            @Override
            public void onChanged(@Nullable List<EventModel> eventModels) {
                if (zoomLayout.isEmpty()) {
                    zoomLayout.setEvents(eventModels);
                }
            }
        });

        deleteArea.setOnDragListener(new AbstractDropToListener() {
            @Override
            protected void onDrop() {
                final EventPoint event = zoomLayout.getFliyingEvent();
                zoomLayout.deleteEvent(event);
            }
        });

        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(campaign.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_event_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_map:
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    if (!hasPermissions(this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_STORAGE);
                    } else {
                        openGallery();
                    }
                } else {
                    openGallery();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();

            // Get the path from the Uri
            final String path = getPathFromURI(selectedImage);
            if (path != null) {
                File f = new File(path);
                selectedImage = Uri.fromFile(f);
            }

            zoomLayout.setBackgroundImage(selectedImage);
            campaign.setBackgroundImage(selectedImage.toString());
            eventMapVM.updateCampaign(campaign);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /*check permissions  for marshmallow*/
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /*get Permissions Result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "The app was not allowed to access to your storage.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
    }
}
