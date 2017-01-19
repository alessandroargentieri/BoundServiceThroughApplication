package alessandro.argentieri.boundservicethroughapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements CallerInterface{

    TextView countText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        countText = (TextView)findViewById(R.id.timestamp_text);
        WholeApplication.setCaller(this);
    }

    //////////////DALL'INTERFACCIA/////////////////////////////////////
    //questo per permettere alla classe Whole application di interfacciarsi
    //con l'interfaccia e non direttamente con l'activity, così da poter fare la stessa operazione su più activity diverse che implementano le stesse operazioni (quelle dell'interfaccia)
    @Override
    public void setCountText(int count){
        countText.setText(count + "");
    }


    ///////////////////////////////////////////////////////////////////

    //////////////BOTTONI//////////////////////////////
    public void Azzera(View v){
        BoundService.azzeraCount();
    }

    public void PassToOtherActivity(View v){
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
