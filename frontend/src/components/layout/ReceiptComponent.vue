<script setup lang="ts">
import { useSlots } from 'vue';

const slots = useSlots();

</script>

<template>
  <div class="receiptpanel">
    <div class="receiptpanel__container">
      <div class="receiptpanel__box">
        <h3>Receipt</h3>
        <hr />
        <ul class="receiptpanel__list">
          <slot name="receipt-list">
            <li>No players available</li>
          </slot>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped lang="css">
h3 {
  font-weight: 500;
  margin-bottom: 24px;
}

hr {
  border: none;
  border-top: 2px dashed #4f9fde;
  margin: 24px auto;
  width: 140px
}

.receiptpanel {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 16px;
  box-sizing: border-box;
  overflow-y: auto;
}

.receiptpanel__container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.receiptpanel__box {
  background-color: white;
  box-shadow: 15px 15px 0px rgba(0, 0, 0, 0.25);
  text-align: center;
  padding: 16px;
  height: 100%;
  overflow: hidden;
}

.receiptpanel__list {
  list-style: none;
  padding: 0;
}

</style>
