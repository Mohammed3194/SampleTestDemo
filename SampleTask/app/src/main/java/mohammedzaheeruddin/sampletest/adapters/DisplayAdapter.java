package mohammedzaheeruddin.sampletest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mohammedzaheeruddin.sampletest.R;
import mohammedzaheeruddin.sampletest.entity.DisplayItem;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */
public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.CardViewHolder>{

    private Context context;
    private List<DisplayItem> displayItem,temporaryList;
    boolean value;

    public DisplayAdapter(Context c, List<DisplayItem> cardItem) {
        this.context = c;
        this.displayItem = cardItem;
        this.temporaryList = new ArrayList<>();
        temporaryList.addAll(cardItem);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        DisplayItem item = (DisplayItem) displayItem.get(position);
        holder.txt_name.setText(item.getCity_Name());
    }

    @Override
    public int getItemCount() {
        return displayItem == null ? 0: displayItem.size();
    }

    public void filter(String charText,ArrayList<DisplayItem> item) {
        temporaryList.clear();
        temporaryList = new ArrayList<>();
        temporaryList.addAll(item);
        displayItem.clear();
        if(charText.length() > 0){
            for(int i=0; i < temporaryList.size(); i++){
                if(null != temporaryList.get(i)){
                    if(temporaryList.get(i).getCity_Name().toLowerCase(Locale.getDefault()).contains(charText)){
                        displayItem.add(temporaryList.get(i));
                    }
                }
            }
        }else{
            displayItem.addAll(temporaryList);
        }

        notifyDataSetChanged();

    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_name;

        public CardViewHolder(View v) {
            super(v);
            txt_name = (TextView) v.findViewById(R.id.txt_name);
        }
    }
}
