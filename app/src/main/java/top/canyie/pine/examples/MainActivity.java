package top.canyie.pine.examples;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.canyie.pine.examples.test.Test;
import top.canyie.pine.examples.test.TestItem;

import static top.canyie.pine.examples.ExampleApp.ALL_TESTS;

import java.lang.reflect.Method;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private TextView output;

    public String getString() {
        return "Hello, Android";
    }

    @SuppressLint("DefaultLocale") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_main);

        try {
            Method m = MainActivity.class.getDeclaredMethod("getString");
            Pine.hook(m, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) throws Throwable {
                    callFrame.setResult("Hello, but hooked");
                }
            });
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        TextView text = findViewById(R.id.textView);
        text.setText(getString());

//        ListView listView = findViewById(R.id.test_list);
//        output = findViewById(R.id.test_output);
//        output.setText(String.format("Android %s (API %d); CPU Arch %s; No Test executed…",
//                Build.VERSION.RELEASE, Build.VERSION.SDK_INT, Build.CPU_ABI));
//
//        String[] testNames = new String[ALL_TESTS.length];
//        for (int i = 0; i < ALL_TESTS.length; i++) {
//            testNames[i] = "Test " + ALL_TESTS[i].name;
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.test_item,
//                R.id.test_item_name, testNames);
//
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
    }

    @SuppressLint("SetTextI18n") @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TestItem testItem = ALL_TESTS[position];
        int result = testItem.run();
        if (result == Test.SUCCESS) {
            output.setText("Test " + testItem.name + " success!");
        } else if (result == Test.FAILED) {
            output.setText("Test " + testItem.name + " failed!");
        } else {
            output.setText("See Toast");
        }
    }
}
