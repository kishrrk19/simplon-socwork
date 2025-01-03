<script>
export default {
  data() {
    return {
      inputs: {
        username: "",
        password: ""
      }
    }
  },
  methods: {
    async submit() {
      const options = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.inputs)

      }
      try {
        const response = await fetch('http://localhost:8080/accounts/login', options);
        const message = await response.text();

        console.log(response);
      if(response.status == 401){
        alert("Bad Credentials")
      }else{
        console.log(message)
      }
        
      } catch (error) {
        console.error('Error occurred:', error);
        alert('An error occurred while processing the request.');
      }
    }
  }
}

</script>

<template>
  <h1>S'authentifier</h1>
  <form @submit.prevent="submit" novalidate>
    <div class="flex">
      <label for="name">User name</label>
      <input type="email" id="username" name="username" v-model="inputs.username">
      <label for="password">Password</label>
      <input type="password" id="password" name="password" v-model="inputs.password">
      <button type="submit">Login</button>
    </div>
  </form>
  <div>{{ inputs.username }}</div>
</template>
<style scoped>
.flex {
  display: flex;
  flex-direction: column;
}

input {
  margin-bottom: 1rem;
}

h1 {
  color: blue;
}

label::after {
  content: "*";
  color: red;
}
</style>