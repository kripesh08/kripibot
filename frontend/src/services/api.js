const API_BASE_URL = '/api';

export const api = {
  // Fetch all conversations
  async getConversations() {
    const res = await fetch(`${API_BASE_URL}/conversations`);
    if (!res.ok) throw new Error('Failed to fetch conversations');
    return res.json();
  },

  // Fetch full conversation details including messages
  async getConversation(id) {
    const res = await fetch(`${API_BASE_URL}/conversations/${id}`);
    if (!res.ok) throw new Error('Failed to fetch conversation');
    return res.json();
  },

  // Create a new conversation
  async createConversation(title = 'New Chat', model = 'gemini-3.6-flash') {
    const params = new URLSearchParams({ title, model });
    const res = await fetch(`${API_BASE_URL}/conversations?${params.toString()}`, {
      method: 'POST',
    });
    if (!res.ok) throw new Error('Failed to create conversation');
    return res.json();
  },

  // Delete a conversation
  async deleteConversation(id) {
    const res = await fetch(`${API_BASE_URL}/conversations/${id}`, {
      method: 'DELETE',
    });
    if (!res.ok) throw new Error('Failed to delete conversation');
    return true;
  },

  // Update conversation title
  async updateTitle(id, title) {
    const res = await fetch(`${API_BASE_URL}/conversations/${id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title }),
    });
    if (!res.ok) throw new Error('Failed to update title');
    return res.json();
  },

  // Send chat message
  async sendMessage({ conversationId, message, model, apiKey, systemPrompt }) {
    const res = await fetch(`${API_BASE_URL}/chat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        conversationId: conversationId || null,
        message,
        model,
        apiKey: apiKey || null,
        systemPrompt: systemPrompt || null,
      }),
    });

    if (!res.ok) {
      const err = await res.json().catch(() => ({ message: res.statusText }));
      throw new Error(err.message || 'Error communicating with KripiBot server');
    }
    return res.json();
  },
};
