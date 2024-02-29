<template>
  <h1>Oving 5: Coding in Web</h1>
  <div class="container">
    <div class="coding">
      <a>
        <text>Input</text>
        <select
          v-model="selectedLanguage"
          class="coding-language"
          ref="coding-language"
        >
          <option value="javascript">JavaScript</option>
          <option value="python">Python</option>
          <option value="java">Java</option>
          <option value="C">C</option>
        </select>
      </a>

      <textarea
        v-model="inputText"
        class="coding-box coding-input"
        ref="coding-input"
      ></textarea>
    </div>
    <div class="coding">
      <h2>Output</h2>
      <output class="coding-box coding-output" ref="coding-output">
        <div v-for="output in outputs" :key="output" class="output">
          {{ output }}
        </div>
      </output>
    </div>
  </div>
  <div @click="compile" class="compile">RUN CODE</div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      selectedLanguage: "java",
      inputText: "",
      outputs: [],
    };
  },
  methods: {
    compile() {
      axios
        .post("http://localhost:8080/api/coding/run", {
          code: this.inputText,
          language: this.selectedLanguage,
        })
        .then((response) => {
          this.outputs.push(response.data.output);
        })
        .catch((error) => {
          this.outputs.push(error);
        });
    },
  },
};
</script>

<style scoped>
h1 {
  margin-top: 15vh;
  color: white;
  font-weight: 700;
  text-align: center;
  cursor: default;
}

text {
  color: white;
  font-size: 24px;
  font-weight: 700;
  text-align: center;
  cursor: default;
  padding-top: 0;
  margin-bottom: 0;
}
.coding-language {
  background-color: transparent;
  color: #fff;
  font-size: 14px;
  border: #ffffff 1px solid;
  outline: none;
  text-align: left;
  cursor: pointer;
  padding: 5px;
  border-radius: 5px;
  transition: border 0.5s ease-in-out;
}

a {
  margin-top: 5%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 20px; /* Add a gap of 10px between the items */
}
.container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.coding {
  width: 400px;
  color: #fff;
  background-color: rgba(66, 66, 66, 0.5);
  margin: 1%;
  border-radius: 10px;
  max-height: 400px;
  min-height: 400px;
  align-items: center;
  cursor: default;
  top: 0;
}
.coding-box {
  margin-top: 5%;
  margin-bottom: 5%;
  flex-flow: column;
  justify-content: center;
  height: 70%;
  overflow: hidden;
  overflow-y: scroll;
}

.coding-input {
  height: 310px;
}

textarea {
  margin-top: 5%;
  margin-bottom: 5%;
  justify-content: left;
  height: 300px;
  width: 90%;
  background-color: transparent;
  color: #fff;
  font-size: 14px;
  border: #ffffff 1px solid;
  outline: none;
  resize: none;
  text-align: left;
}
.coding-output {
  margin-top: 5%;
  margin-bottom: 5%;
  flex-flow: column;
  justify-content: center;
  height: 70%;
  overflow: hidden;
  overflow-y: scroll;
}

.compile {
  text-decoration: none;
  margin-top: 5%;
  margin-bottom: 5%;
  text-align: center;
  color: white;
  font-size: 20px;
  cursor: pointer;
  transition: color 0.5s ease-in-out, -webkit-text-stroke 0.5s ease-in-out;
  align-items: center;
  position: relative;
  display: inline-block;
}

.compile::after {
  content: "";
  position: absolute;
  width: 100%;
  height: 2px;
  bottom: -2px;
  left: 0;
  background-color: white;
  transform: scaleX(0);
  transition: transform 0.3s ease-in-out;
}

.compile:hover::after {
  transform: scaleX(1);
}
</style>
