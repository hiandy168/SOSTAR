package com.renyu.commonlibrary.views;

import android.support.v4.app.FragmentManager;

/**
 * Created by renyu on 2017/2/22.
 */

public class ActionSheetUtils {

    public static void showDate(FragmentManager manager, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.DATE)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show();
    }

    public static void showTime(FragmentManager manager, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.TIME)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show();
    }

    public static void showList(FragmentManager manager, String title, String[] items, ActionSheetFragment.OnItemClickListener onItemClickListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.ITEM)
                .setTitle(title).setListItems(items, onItemClickListener)
                .setOnCancelListener(onCancelListener).show();
    }
}
