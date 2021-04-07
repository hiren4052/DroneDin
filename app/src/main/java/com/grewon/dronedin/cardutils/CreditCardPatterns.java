package com.grewon.dronedin.cardutils;

import android.content.Context;


import com.grewon.dronedin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kauserali on 05/05/14.
 */
public class CreditCardPatterns implements CreditCardEditText.CreditCartEditTextInterface {

    private Context context;

    public CreditCardPatterns(Context context) {
        this.context = context;
    }

    @Override
    public List<CreditCardEditText.CreditCard> mapOfRegexStringAndImageResourceForCreditCardEditText(CreditCardEditText creditCardEditText) {
        List<CreditCardEditText.CreditCard> listOfPatterns = new ArrayList<CreditCardEditText.CreditCard>();

        CreditCardEditText.CreditCard visa = new CreditCardEditText.CreditCard("^4[0-9]{12}(?:[0-9]{3})?$", context.getResources().getDrawable(R.drawable.ic_visa), CreditCardTypeEnum.VISA.cartType);
        CreditCardEditText.CreditCard mastercard = new CreditCardEditText.CreditCard("^5[1-5][0-9]{14}$", context.getResources().getDrawable(R.drawable.ic_mastercard), CreditCardTypeEnum.MASTER_CARD.cartType);
        CreditCardEditText.CreditCard amex = new CreditCardEditText.CreditCard("^3[47][0-9]{13}$", context.getResources().getDrawable(R.drawable.amex), CreditCardTypeEnum.AMERICAN_EXPRESS.cartType);
        CreditCardEditText.CreditCard diners = new CreditCardEditText.CreditCard("^3(?:0[0-5]|[68][0-9])[0-9]{11}$", context.getResources().getDrawable(R.drawable.diners), CreditCardTypeEnum.DINERS.cartType);
        CreditCardEditText.CreditCard rupay = new CreditCardEditText.CreditCard("^6[0-9]{15}$", context.getResources().getDrawable(R.drawable.rupay), CreditCardTypeEnum.RUPAY.cartType);
        CreditCardEditText.CreditCard jcb = new CreditCardEditText.CreditCard("^(?:2131|1800|35\\d{3})\\d{11}$", context.getResources().getDrawable(R.drawable.jcb), CreditCardTypeEnum.JCB.cartType);
        CreditCardEditText.CreditCard discover = new CreditCardEditText.CreditCard("^6011-?\\d{4}-?\\d{4}-?\\d{4}$", context.getResources().getDrawable(R.drawable.ds), CreditCardTypeEnum.DISCOVER.cartType);

        listOfPatterns.add(visa);
        listOfPatterns.add(mastercard);
        listOfPatterns.add(amex);
        listOfPatterns.add(diners);
        listOfPatterns.add(rupay);
        listOfPatterns.add(jcb);
        listOfPatterns.add(discover);

        return listOfPatterns;
    }
}