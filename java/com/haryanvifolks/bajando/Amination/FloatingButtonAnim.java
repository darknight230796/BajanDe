package com.haryanvifolks.bajando.Amination;

import android.view.View;
import android.view.animation.AnimationUtils;

import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.haryanvifolks.bajando.MainActivity.next_button;
import static com.haryanvifolks.bajando.MainActivity.play_menu_button;
import static com.haryanvifolks.bajando.MainActivity.previous_button;
import static com.haryanvifolks.bajando.MainActivity.repeat_button;
import static com.haryanvifolks.bajando.MainActivity.shuffle_button;
import static com.haryanvifolks.bajando.MainActivity.togglePlayButton;

public class FloatingButtonAnim {
    private CoordinatorLayout.LayoutParams layoutParams;

    public void togglePlayButton(){
        if (togglePlayButton)
            closeMenu();
        else
            openMenu();
    }

    public void openMenu(){

        MainActivity.togglePlayButton = true;

        layoutParams = (CoordinatorLayout.LayoutParams) previous_button.getLayoutParams();
        layoutParams.rightMargin += (int) (previous_button.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (previous_button.getHeight() * 0.25);
        previous_button.setLayoutParams(layoutParams);
        previous_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.show_previous_button));
        previous_button.setClickable(true);

        layoutParams = (CoordinatorLayout.LayoutParams) next_button.getLayoutParams();
        layoutParams.rightMargin -= (int) (next_button.getWidth() * 1.2);
        layoutParams.bottomMargin += (int) (next_button.getHeight() * 0.25);
        next_button.setLayoutParams(layoutParams);
        next_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.show_next_button));
        next_button.setClickable(true);

        layoutParams = (CoordinatorLayout.LayoutParams) play_menu_button.getLayoutParams();
        layoutParams.rightMargin += (int) (play_menu_button.getWidth() * 0.25);
        layoutParams.bottomMargin += (int) (play_menu_button.getHeight() * 1.5);
        play_menu_button.setLayoutParams(layoutParams);
        play_menu_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.show_play_menu_button));
        play_menu_button.setClickable(true);

        layoutParams = (CoordinatorLayout.LayoutParams) shuffle_button.getLayoutParams();
        layoutParams.rightMargin -= (int) (shuffle_button.getWidth() * 0.75);
        layoutParams.bottomMargin += (int) (shuffle_button.getHeight() * 1.25);
        shuffle_button.setLayoutParams(layoutParams);
        shuffle_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.show_shuffle_button));
        shuffle_button.setClickable(true);

        layoutParams = (CoordinatorLayout.LayoutParams) repeat_button.getLayoutParams();
        layoutParams.rightMargin += (int) (repeat_button.getWidth() * 1.25);
        layoutParams.bottomMargin += (int) (repeat_button.getHeight() * 1.25);
        repeat_button.setLayoutParams(layoutParams);
        repeat_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.show_repeat_button));
        repeat_button.setClickable(true);

    }
    public void closeMenu(){
        MainActivity.togglePlayButton = false;

        layoutParams = (CoordinatorLayout.LayoutParams) previous_button.getLayoutParams();
        layoutParams.rightMargin -= (int) (previous_button.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (previous_button.getHeight() * 0.25);
        previous_button.setLayoutParams(layoutParams);
        previous_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.hide_previous_button));
        previous_button.setClickable(false);
        previous_button.setVisibility(View.INVISIBLE);

        layoutParams = (CoordinatorLayout.LayoutParams) next_button.getLayoutParams();
        layoutParams.rightMargin += (int) (next_button.getWidth() * 1.2);
        layoutParams.bottomMargin -= (int) (next_button.getHeight() * 0.25);
        next_button.setLayoutParams(layoutParams);
        next_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.hide_next_button));
        next_button.setClickable(false);
        next_button.setVisibility(View.INVISIBLE);

        layoutParams = (CoordinatorLayout.LayoutParams) play_menu_button.getLayoutParams();
        layoutParams.rightMargin -= (int) (play_menu_button.getWidth() * 0.25);
        layoutParams.bottomMargin -= (int) (play_menu_button.getHeight() * 1.5);
        play_menu_button.setLayoutParams(layoutParams);
        play_menu_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.hide_play_menu_button));
        play_menu_button.setClickable(false);
        play_menu_button.setVisibility(View.INVISIBLE);

        layoutParams = (CoordinatorLayout.LayoutParams) shuffle_button.getLayoutParams();
        layoutParams.rightMargin += (int) (shuffle_button.getWidth() * 0.75);
        layoutParams.bottomMargin -= (int) (shuffle_button.getHeight() * 1.25);
        shuffle_button.setLayoutParams(layoutParams);
        shuffle_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.hide_shuffle_button));
        shuffle_button.setClickable(false);
        shuffle_button.setVisibility(View.INVISIBLE);

        layoutParams = (CoordinatorLayout.LayoutParams) repeat_button.getLayoutParams();
        layoutParams.rightMargin -= (int) (repeat_button.getWidth() * 1.25);
        layoutParams.bottomMargin -= (int) (repeat_button.getHeight() * 1.25);
        repeat_button.setLayoutParams(layoutParams);
        repeat_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.getMainContext(),R.anim.hide_repeat_button));
        repeat_button.setClickable(false);
        repeat_button.setVisibility(View.INVISIBLE);

    }
}
