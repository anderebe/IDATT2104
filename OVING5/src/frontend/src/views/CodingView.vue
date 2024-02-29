<template>
  <div class="coding">
    <NavHeader />
    <Coding />
  </div>
</template>

<script>
import NavHeader from "@/components/NavHeader.vue";
import Coding from "@/components/CodingContent.vue";

export default {
  name: "CodingView",
  components: {
    Coding,
    NavHeader,
  },
};
</script>
