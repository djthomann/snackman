import { defineStore } from 'pinia';

export const useGameStore = defineStore('game', {
  state: () => ({
    remainingTime: 0,
    calories: 0,
  }),
  actions: {
    setRemainingTime(time: number) {
      this.remainingTime = time;
    },
    setCalories(amount: number) {
      this.calories = amount;
      console.log(this.calories);
    }
  },
});
