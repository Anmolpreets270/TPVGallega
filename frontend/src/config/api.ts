import axios from 'axios';

import { API_BASE_URL } from './env';

export const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: { 'Content-Type': 'application/json' },
});
