package alessandro.argentieri.boundservicethroughapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CallerInterface{

    TextView countText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countText = (TextView)findViewById(R.id.timestamp_text);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //pass the reference of the current Activity to the static method of WholeApplication (that's why we don't create an instance of it): it's necessary to keep track to the Activity which is currently engaged in the communication with the Service started and bound from the WholeApplication. setCaller, to be adapted to both MainActivity type and Main2Activity type requires the CallerInterface which both the two kinds of Activities implement.
        WholeApplication.setCaller(this);
    }


    //////////////DALL'INTERFACCIA/////////////////////////////////////
    //questo per permettere alla classe Whole application di interfacciarsi genericamente con l'interfaccia CallerInterface e chiamare, quindi, o MainActivity o Main2Activity a seconda di quale oggetto gli abbiamo precedentemente passato tramite il metodo statico setCaller()
    //con l'interfaccia e non direttamente con l'activity, così da poter fare la stessa operazione su più activity diverse che implementano le stesse operazioni (quelle dell'interfaccia)
    @Override
    public void setCountText(int count){
        countText.setText(count + "");
    }

    ///////////////////////////////////////////////////////////////////


    //////////////BOTTONI//////////////////////////////
    public void Azzera(View v){
        //chiede a Whole Application di azzerare il count: l'applicazione rimanderà lo stesso comando al Service
        WholeApplication.azzeraCount();
     }

    public void PassToOtherActivity(View v){
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

}
