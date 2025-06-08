package org.example.dsassignment3_4.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import org.controlsfx.control.WorldMapView;
import org.example.dsassignment3_4.utilities.UtilityMethods;

import java.net.URL;
import java.util.ResourceBundle;

public class WorldMapViewController implements Initializable {

    @FXML
    private WorldMapView worldMapView;

    private String selectedLocationName;

    public static WorldMapViewController instance;

    public WorldMapViewController() {
        if (instance == null) {
            instance = this;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Show only India on the map
        worldMapView.setCountries(FXCollections.observableArrayList(WorldMapView.Country.IN));

        // Define locations using valid latitude and longitude
        WorldMapView.Location punjab = new WorldMapView.Location("Punjab", 31.1471, 75.3412);
        WorldMapView.Location uttarakhand = new WorldMapView.Location("Uttarakhand", 30.0668, 79.0193);
        WorldMapView.Location delhi = new WorldMapView.Location("Delhi", 28.7041, 77.1025);
        WorldMapView.Location gujarat = new WorldMapView.Location("Gujarat", 22.2587, 71.1924);
        WorldMapView.Location mumbai = new WorldMapView.Location("Mumbai", 19.0760, 72.8777);
        WorldMapView.Location bihar = new WorldMapView.Location("Bihar", 25.0961, 85.3131);

        ObservableList<WorldMapView.Location> locations = FXCollections.observableArrayList(
                punjab, uttarakhand, delhi, gujarat, mumbai, bihar);

        // Show locations on the map
        worldMapView.setShowLocations(true);
        worldMapView.setLocations(locations);

        // Optional: Show tooltip of the first location on hover
        worldMapView.setTooltip(new Tooltip("Click on any city"));

        // Handle click events
        worldMapView.setOnMouseClicked(event -> {
            ObservableList<WorldMapView.Location> selectedLocations = worldMapView.getSelectedLocations();
            ObservableList<WorldMapView.Country> countries = worldMapView.getSelectedCountries();

            if (!selectedLocations.isEmpty()) {
                selectedLocationName = selectedLocations.get(0).getName();
                UtilityMethods.showPopup("You selected: " + selectedLocationName);
                Stage stage = (Stage) worldMapView.getScene().getWindow();
                stage.close();
            }

            if (!countries.isEmpty()) {
                UtilityMethods.showPopup("You selected: " + countries.get(0).getLocale().getDisplayName());
            }
        });
    }

    public String getSelectedLocation() {
        return selectedLocationName;
    }
}
