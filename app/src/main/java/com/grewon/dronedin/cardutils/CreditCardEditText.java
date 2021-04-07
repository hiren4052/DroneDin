package com.grewon.dronedin.cardutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;


import com.grewon.dronedin.R;

import java.util.List;

/**
 * Created by kauserali on 05/05/14.
 */
public class CreditCardEditText extends androidx.appcompat.widget.AppCompatEditText implements CreditCardTextWatcher.TextWatcherListener {

    private static final String SEPARATOR = "-";
    private static int DEFAULT_NO_MATCH_FOUND_DRAWABLE = R.drawable.credit_card;

    /**
     * Default minimum and maximum card length.
     */
    private static final int MINIMUM_CREDIT_CARD_LENGTH = 13;
    private static final int MAXIMUM_CREDIT_CARD_LENGTH = 19;

    /**
     * List of CreditCard objects containing the image to display
     * and the regex for pattern matching.
     */
    private List<CreditCard> mListOfCreditCardChecks;

    /**
     * This drawable is shown by default and when no match is found
     */
    private Drawable mNoMatchFoundDrawable;
    private CreditCard mCurrentCreditCardMatch;
    private CreditCartEditTextInterface mCreditCardEditTextInterface;
    private CreditCardTextWatcher mTextWatcher;

    private String mSeparator;
    private int mMinimumCreditCardLength, mMaximumCreditCardLength;
    private String mPreviousText;

    public interface CreditCartEditTextInterface {
        List<CreditCard> mapOfRegexStringAndImageResourceForCreditCardEditText(CreditCardEditText creditCardEditText);
    }

    public CreditCardEditText(Context context) {
        super(context);
        init();
        mSeparator = SEPARATOR;
    }

    public CreditCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initPropertiesFromAttributes(context, attrs);
    }

    public CreditCardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        initPropertiesFromAttributes(context, attrs);
    }

    public Drawable getNoMatchFoundDrawable() {
        return mNoMatchFoundDrawable;
    }

    public void setNoMatchFoundDrawable(Drawable noMatchFoundDrawable) {
        if (noMatchFoundDrawable != null) {
            mNoMatchFoundDrawable = noMatchFoundDrawable;
            mNoMatchFoundDrawable.setBounds(0, 0, mNoMatchFoundDrawable.getIntrinsicWidth(), mNoMatchFoundDrawable.getIntrinsicHeight());
            showRightDrawable(null);
        }
    }

    public void setCreditCardEditTextListener(CreditCartEditTextInterface creditCartEditTextInterface) {
        mCreditCardEditTextInterface = creditCartEditTextInterface;
        if (mCreditCardEditTextInterface != null) {
            mListOfCreditCardChecks = mCreditCardEditTextInterface.mapOfRegexStringAndImageResourceForCreditCardEditText(this);
        }
    }

    public String getTypeOfSelectedCreditCard() {
        if (mCurrentCreditCardMatch != null) {
            return mCurrentCreditCardMatch.getType();
        }
        return null;
    }

    public int getMaximumCreditCardLength() {
        return mMaximumCreditCardLength;
    }

    public void setMaximumCreditCardLength(int maximumCreditCardLength) {
        mMaximumCreditCardLength = maximumCreditCardLength;
    }

    public int getMinimumCreditCardLength() {
        return mMinimumCreditCardLength;
    }

    public void setMinimumCreditCardLength(int minimumCreditCardLength) {
        mMinimumCreditCardLength = minimumCreditCardLength;
    }

    public String getCreditCardNumber() {
        String creditCardNumber = getText().toString().replace(mSeparator, "");
        if (creditCardNumber.length() >= mMinimumCreditCardLength && creditCardNumber.length() <= mMaximumCreditCardLength) {
            return creditCardNumber;
        }
        return null;
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        matchRegexPatternsWithText(text.replace(mSeparator, ""));

        if (mPreviousText != null && text.length() > mPreviousText.length()) {
            String difference = StringUtil.difference(text, mPreviousText);
            if (!difference.equals(mSeparator)) {
                addSeparatorToText();
            }
        }
        mPreviousText = text;
    }

    public static class CreditCard {
        private String mRegexPattern;
        private Drawable mDrawable;
        private String mType;

        public CreditCard(String regexPattern, Drawable drawable, String type) {
            if (regexPattern == null || drawable == null || type == null) {
                throw new IllegalArgumentException();
            }
            mRegexPattern = regexPattern;
            mDrawable = drawable;
            mType = type;
        }

        public String getRegexPattern() {
            return mRegexPattern;
        }

        public Drawable getDrawable() {
            return mDrawable;
        }

        public String getType() {
            return mType;
        }
    }

    private void initPropertiesFromAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardEditText);
        if (typedArray != null) {
            mSeparator = typedArray.getString(R.styleable.CreditCardEditText_separator);
            if (mSeparator == null) {
                mSeparator = SEPARATOR;
            }
            typedArray.recycle();
        }
    }

    private void init() {
        mMinimumCreditCardLength = MINIMUM_CREDIT_CARD_LENGTH;
        mMaximumCreditCardLength = MAXIMUM_CREDIT_CARD_LENGTH;
        mNoMatchFoundDrawable = getResources().getDrawable(DEFAULT_NO_MATCH_FOUND_DRAWABLE);
        mNoMatchFoundDrawable.setBounds(0, 0, mNoMatchFoundDrawable.getIntrinsicWidth(), mNoMatchFoundDrawable.getIntrinsicHeight());

        setInputType(InputType.TYPE_CLASS_NUMBER);

        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mNoMatchFoundDrawable, getCompoundDrawables()[3]);
        if (mCreditCardEditTextInterface != null) {
            mListOfCreditCardChecks = mCreditCardEditTextInterface.mapOfRegexStringAndImageResourceForCreditCardEditText(this);
        }
        mTextWatcher = new CreditCardTextWatcher(this, this);
        addTextChangedListener(mTextWatcher);
        setCreditCardEditTextListener(new CreditCardPatterns(getContext()));
    }

    private void addSeparatorToText() {
        String text = getText().toString();
        text = text.replace(mSeparator, "");
        if (text.length() >= 16) {
            return;
        }
        int interval = 4;
        char separator = mSeparator.charAt(0);

        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = 0; i < text.length() / interval; i++) {
            stringBuilder.insert(((i + 1) * interval) + i, separator);
        }
        removeTextChangedListener(mTextWatcher);
        setText(stringBuilder.toString());
        setSelection(getText().length());
        addTextChangedListener(mTextWatcher);
    }

    private void matchRegexPatternsWithText(String text) {
        if (mListOfCreditCardChecks != null && mListOfCreditCardChecks.size() > 0) {
            Drawable drawable = null;
            for (CreditCard creditCard : mListOfCreditCardChecks) {
                String regex = creditCard.getRegexPattern();
                if (text.matches(regex)) {
                    mCurrentCreditCardMatch = creditCard;
                    drawable = creditCard.getDrawable();
                    break;
                }
            }
            showRightDrawable(drawable);
        }
    }

    private void showRightDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], drawable, getCompoundDrawables()[3]);
        } else {
            mCurrentCreditCardMatch = null;
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mNoMatchFoundDrawable, getCompoundDrawables()[3]);
        }
    }
}
