package com.ai_keys.iot.ui.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;

import com.ai_keys.iot.R;

public class NoBgDialog extends AlertDialog
{
    public NoBgDialog(Context context)
    {
        super(context, R.style.TransparentBgDialog);
    }
}
