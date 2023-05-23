package com.example.dasmeet;

public class TimerEvent {

        private long millisUntilFinished;

        public TimerEvent(long millisUntilFinished) {
            this.millisUntilFinished = millisUntilFinished;
        }

        public long getMillisUntilFinished() {
            return millisUntilFinished;
        }


}
