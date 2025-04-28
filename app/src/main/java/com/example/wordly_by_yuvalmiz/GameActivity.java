package com.example.wordly_by_yuvalmiz;


import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private BoardGame boardGame;
    TextToSpeech textToSpeech;
    SoundPool soundPool;
    int sound1,sound2;
    FbModuleGame fbModuleGame;

    LinearLayout linearLayout, linearLayout1, linearLayout2,linearLayout3;
    KeyboardView m;
    Button save,quit;


    private String targetWord;
    private int attempts=0;
    String result = null;
    ArrayList<String> userguesses;
    String random5LetterWord = "https://random-word-api.vercel.app/api?words=1&length=5";
    String userGuess;
    int Greenflag =0; //count of how many letters are correct
    private DownloadJson downloanJson;

    public void transferUserGuess(String str) {
        userGuess = str;
        startGame(userGuess);
    }

    @Override
    public void onClick(View view) {
        if (view == quit)
        {
            finish();
        }
        if ((view == save))
        {
            fbModuleGame.setUserGuess(userGuess);
        }
    }

    public class DownloadJson extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;
            InputStreamReader inputStreamReader;

            try {
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data != -1)
                {
                    result += (char)data;
                    data = inputStreamReader.read();
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return result;
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fbModuleGame = new FbModuleGame(this);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);

        sound1 = soundPool.load(this,R.raw.wrongsound,1);
        sound2 = soundPool.load(this,R.raw.correctsound,1);


        linearLayout = findViewById(R.id.activitygame);
        linearLayout1 = findViewById(R.id.firstLayout);
        linearLayout2 = findViewById(R.id.secondLayout);
        linearLayout3 = findViewById(R.id.thirdLayout);

        quit = findViewById(R.id.btnquit);
        save = findViewById(R.id.btnsave);
        quit.setOnClickListener(this);
        save.setOnClickListener(this);



        boardGame = new BoardGame(this);
        linearLayout2.addView(boardGame);
        m=new KeyboardView(this);
        linearLayout3.addView(m);

        Intent i = getIntent();
        String color = i.getStringExtra("color");
        setBackgroundColor(color);

        String url = random5LetterWord;

        downloanJson = new DownloadJson();

        try {
            result = downloanJson.execute(url).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
                throw new RuntimeException(e);
        }
        targetWord = result.replaceAll("[\\[\\]\" ]", "");
        Toast.makeText(this, ""+targetWord, Toast.LENGTH_SHORT).show();

        if(fbModuleGame.CheckIfSaved()){
            userguesses = fbModuleGame.ReturnSavedGuesses();
            int size = userguesses.size();
            for (int j = 0; j < size; j++) {
                String word = userguesses.get(j);
                startGame(word);
            }
        }

    }


    public void onGuessesLoaded(ArrayList<String> guesses) {
        userguesses = guesses;
        attempts = guesses.size(); // set current attempt index

        for (int i = 0; i < guesses.size(); i++) {
            boardGame.setNewWord(i, guesses.get(i));
        }
        for (int i = 0; i < guesses.size(); i++) {
            colorGuessRow(i, guesses.get(i));
        }

    }

    public void resetGame()
    {
        String url = random5LetterWord;

        downloanJson = new DownloadJson();

        try {
            result = downloanJson.execute(url).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        targetWord = result.replaceAll("[\\[\\]\" ]", "");
        Toast.makeText(this, ""+targetWord, Toast.LENGTH_SHORT).show();

        attempts =0;
        boardGame = new BoardGame(this);
        linearLayout2.removeAllViews();
        linearLayout2.addView(boardGame);

    }



    private void startGame(String userGuess) {
        if (attempts < 6) {
            if (userGuess.length() != 5) {
                Toast.makeText(this, "Your word is not 5 letters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isWordValid(userGuess)) {
                Toast.makeText(this, "Your word does not exist in English", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isWordValid(userGuess) && userGuess.length() == 5) {
                for (int i = 0; i < 5; i++) {
                    if (userGuess.charAt(i) == targetWord.charAt(i)) {
                        boardGame.setCellBackgroundColor(attempts, i, Color.GREEN);
                        Greenflag++;
                    } else if (YellowSquare(userGuess.charAt(i)))
                        boardGame.setCellBackgroundColor(attempts, i, Color.YELLOW);
                    else
                        boardGame.setCellBackgroundColor(attempts, i, Color.RED);
                }
                boardGame.setNewWord(attempts,userGuess);
                attempts++;
            }

            if(Greenflag == 5)
            {
                soundPool.play(sound2,1,1,0,3,1);
                textToSpeech.speak("Congratulation you have won", TextToSpeech.QUEUE_FLUSH, null);
                boardGame.triggerWinAnimation(); // 💥 START THE SPARKLE SHOW
                fbModuleGame.WinState();
                createDialog();
            }
            Greenflag =0;
        }
        if(attempts == 6)
        {
            soundPool.play(sound1,1,1,0,0,1);
            fbModuleGame.WinState();
            createDialog();
        }

    }

    private boolean isWordValid(String word) {
        String API_KEY = "8e8sodqspul7qzwvfocimwsu3g9tua7qg6ktlgx13zptyo57h";  // Replace with your Wordnik API key
        String url = "https://api.wordnik.com/v4/word.json/" + word + "/definitions?api_key=" + API_KEY;

        DownloadJson downloadJson = new DownloadJson();

        try {
            // Perform the network operation to get the JSON response
            String result = downloadJson.execute(url).get();

            // Check if the result is empty or null
            if (result == null || result.isEmpty()) {
                return false; // If the response is empty, the word is not valid
            }

            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(result);

            // If there are any definitions (jsonArray length > 0), proceed to check further
            if (jsonArray.length() > 0) {
                // Loop through all the definitions in the JSON response
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject definition = jsonArray.getJSONObject(i);

                    // Check if the definition has a valid "text" field with a non-empty value
                    if (definition.has("text") && !definition.getString("text").isEmpty()) {
                        String definitionText = definition.getString("text").trim();

                        // Check if the definition text is not just empty or whitespace
                        if (!definitionText.isEmpty() && !definitionText.equalsIgnoreCase("no definition found")) {
                            return true; // The word has at least one valid definition with content
                        }
                    }
                }
            }

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace(); // Handle exceptions (network issues, JSON parsing issues)
        }

        // If no valid definition was found, return false
        return false;
    }


    private void createDialog() {
        CustomDialog customDialog = new CustomDialog(this);
        textToSpeech.speak("Would you like to play again?", TextToSpeech.QUEUE_FLUSH, null);
        customDialog.show();


    }
    private boolean YellowSquare(char a) {
        for (int j = 0; j <5; j++) {
            if (a ==targetWord.charAt(j))
                return true;
        }
        return false;
    }
    private void colorGuessRow(int row, String guess) {
        for (int col = 0; col < 5; col++) {
            char guessChar = guess.charAt(col);
            char correctChar = targetWord.charAt(col);

            if (guessChar == correctChar) {
                boardGame.setCellBackgroundColor(row, col, Color.GREEN);
            } else if (targetWord.indexOf(guessChar) != -1) {
                boardGame.setCellBackgroundColor(row, col, Color.YELLOW);
            } else {
                boardGame.setCellBackgroundColor(row, col, Color.RED);
            }
        }
    }

    public void setBackgroundColor(String str) {
        switch (str)
        {
            case"Blue":
            {
                linearLayout.setBackgroundColor(Color.BLUE);
                break;
            }
            case"Purple":
            {
                linearLayout.setBackgroundColor(Color.argb(255,167,29,216));
                break;

            }
            case"Orange":
            {
                linearLayout.setBackgroundColor(Color.argb(255,217,105,28));
                break;

            }


            case "Pink":
            {
                linearLayout.setBackgroundColor(Color.argb(255,255,192,203));
                break;
            }
            case "Cyan":
            {
                linearLayout.setBackgroundColor(Color.argb(255, 2,227,245));
                break;
            }
            case "Purple_light":
            {
                linearLayout.setBackgroundColor(Color.argb(255, 125,0 , 255));
                break;
            }

            default:
                break;


        }
    }
}
