package nure.ananiev.nestorian.pract_task_4;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    private final List<Person> people;

    public PeopleAdapter(List<Person> people) {
        this.people = people;
    }

    public void addPerson(Person person) {
        people.add(person);
        notifyItemInserted(people.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView ageTextView;

        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.name_text_view);
            ageTextView = v.findViewById(R.id.age_text_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = people.get(position);

        holder.nameTextView.setText(person.getName());
        holder.ageTextView.setText(Integer.toString(person.getAge()));
    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}