package com.example.johan.geoquiz;

/**
 * Created by johan on 11/17/2014.
 */
public class TrueFalse extends QuizActivity {
    private int mQuestion;
    private boolean mTrueQuestion;
    public TrueFalse(int question, boolean trueQuestion) {
        mQuestion = question;
        mTrueQuestion = trueQuestion;
    }

    public int getQuestion() {
        return mQuestion;
    }

    private int setQuestion(int question) {
        return mQuestion = question;
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }

    private void setTrueQuestion(boolean trueQuestion) {
        mTrueQuestion = trueQuestion;
    }


}
