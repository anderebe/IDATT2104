import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import CalculatorView from "../views/CalculatorView.vue";
import FeedbackView from "../views/FeedbackView.vue";
import CodingView from "../views/CodingView.vue";

const router = createRouter({
  history: createWebHistory(),
  mode: "history",
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/calculator",
      name: "calculator",
      component: CalculatorView,
    },
    {
      path: "/feedback",
      name: "feedback",
      component: FeedbackView,
    },
    {
      path: "/coding",
      name: "coding",
      component: CodingView,
    },
    {
      path: "/logout",
      name: "logout",
      component: HomeView,
    },
  ],
});

export default router;
