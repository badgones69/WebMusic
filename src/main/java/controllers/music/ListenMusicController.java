package controllers.music;

import db.MusiqueDb;
import dto.MusiqueDto;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import listeners.music.ListMusicSelectionListener;
import mapper.MusiqueMapper;
import utils.ListenUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ListenMusicController implements Initializable {

    // MEDIA PLAYER
    private static MediaPlayer mediaPlayer;
    // "PLAY" ICON
    private Image playButtonImage = new Image(getClass().getResourceAsStream("/icons/playing.png"));
    // "PAUSE" ICON
    private Image pauseButtonImage = new Image(getClass().getResourceAsStream("/icons/pause.png"));
    // MEDIA PLAYER'S TOTAL LENGTH
    private Duration totalLength;

    // MEDIA PLAYER LISTENER
    private ChangeListener<Duration> mediaPlayerListener;

    /**
     * MUSIC LISTENING COMPONENTS
     */

    @FXML
    private MediaView musicListeningMediaView = new MediaView();

    @FXML
    private ImageView playPauseImageView = new ImageView();

    @FXML
    private Label listeningTitleLabel = new Label();

    @FXML
    private Label listeningArtistsLabel = new Label();

    @FXML
    private Label listeningCurrentTimeLabel = new Label();

    @FXML
    private Slider listeningProgressionSlider = new Slider();

    @FXML
    private Label listeningTotalLengthLabel = new Label();

    // GETTER
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * MUSIC LISTENING COMPONENTS INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.advancedInitialize();
    }

    private void advancedInitialize() {

        InvalidationListener listeningProgressionSliderListener = observable -> {
            double newTime = (listeningProgressionSlider.getValue() * totalLength.toMillis()) / 100;
            mediaPlayer.seek(new Duration(newTime));
        };

        mediaPlayerListener = (observable, oldValue, newValue) -> {
            listeningProgressionSlider.valueProperty().removeListener(listeningProgressionSliderListener);
            String progressionTimesFormatted = ListenUtils.formatTime(newValue, totalLength);
            listeningCurrentTimeLabel.setText(progressionTimesFormatted.substring(0, progressionTimesFormatted.indexOf('/')));
            listeningProgressionSlider.setDisable(totalLength.isUnknown());
            listeningProgressionSlider.setValue(newValue.toSeconds() / totalLength.toSeconds() * 100.0);
            if (!listeningProgressionSlider.isDisabled() && totalLength.greaterThan(Duration.ZERO) && !listeningProgressionSlider.isValueChanging()) {
                listeningProgressionSlider.setValue(newValue.toMillis() / (totalLength.toMillis()) * 100.0);
            }
            listeningProgressionSlider.valueProperty().addListener(listeningProgressionSliderListener);
        };
        this.basicInitialize();
    }

    public void basicInitialize() {
        // MEDIA PLAYER
        MusiqueDto musiqueDto = ListMusicSelectionListener.getMusiqueSelected();
        MusiqueDb musiqueDb = MusiqueMapper.toDb(musiqueDto);
        String musicPath = musiqueDb.getNomFichierMusique();
        Media media = new Media(new File(musicPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> totalLength = mediaPlayer.getMedia().getDuration());
        mediaPlayer.currentTimeProperty().addListener(mediaPlayerListener);

        // "PLAY"/"PAUSE" BUTTON
        this.playPauseImageView.setImage(this.playButtonImage);

        // OTHERS
        this.listeningTitleLabel.setText(musiqueDb.getTitreMusique());
        this.listeningProgressionSlider.setValue(Duration.ZERO.toSeconds());
        this.listeningArtistsLabel.setText(musiqueDto.getAuteurs());
        this.listeningCurrentTimeLabel.setText("00:00");
        this.listeningTotalLengthLabel.setText(musiqueDb.getDureeMusique().substring(3));
        this.musicListeningMediaView.setMediaPlayer(mediaPlayer);
    }

    // "PLAY"/"PAUSE" ICON CLICKED
    public void playPauseButtonClicked() {
        if (this.playPauseImageView.getImage().equals(this.pauseButtonImage)) {
            this.playPauseImageView.setImage(playButtonImage);
            mediaPlayer.pause();
        } else {
            this.playPauseImageView.setImage(pauseButtonImage);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(this::basicInitialize);
        }
    }
}