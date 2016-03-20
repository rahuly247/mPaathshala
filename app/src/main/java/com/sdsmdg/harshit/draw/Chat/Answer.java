package com.sdsmdg.harshit.draw.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.harshit.draw.R;

public class Answer extends AppCompatActivity {

    TextView questionTextView, answerTextView;
    String question, answer;
    OnlineDBManager dbManager;
    EditText answerInput;
    Button answerSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent i = getIntent();

        final String id = i.getStringExtra("QueryId");

        question = i.getStringExtra("question");
        answer = i.getStringExtra("answer");

        questionTextView = (TextView) findViewById(R.id.questionTextView);
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        answerInput = (EditText) findViewById(R.id.answerInput);
        answerSubmitButton = (Button) findViewById(R.id.answerSubmitButton);

        questionTextView.setText(question);
        answerTextView.setText(answer);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dbManager = new OnlineDBManager("https://iitjee.azurewebsites.net", getApplicationContext());
            }
        }).start();

        if(answer.equals(""))
        {
            answerTextView.setVisibility(View.GONE);
            answerInput.setVisibility(View.VISIBLE);
            answerSubmitButton.setVisibility(View.VISIBLE);
        }

        answerSubmitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempAnswer = answerInput.getText().toString();
                final Query tempQuery = new Query(question, tempAnswer);
                tempQuery.setId(id);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbManager.update(tempQuery);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Answer successfully posted!", Toast.LENGTH_SHORT).show();
                                answerInput.setVisibility(View.GONE);
                                answerTextView.setVisibility(View.VISIBLE);
                                answerTextView.setText(tempQuery.getmAnswer());
                                answerSubmitButton.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
            }
        });

    }



}
