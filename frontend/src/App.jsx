import React, { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import ChatArea from './components/ChatArea';
import SettingsModal from './components/SettingsModal';
import { api } from './services/api';
import { AlertCircle } from 'lucide-react';

export default function App() {
  const [conversations, setConversations] = useState([]);
  const [currentConvId, setCurrentConvId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

  // User Settings
  const [model, setModel] = useState(() => {
    const saved = localStorage.getItem('kripibot_gemini_model');
    return (saved && !saved.includes('1.5') && !saved.includes('2.5')) ? saved : 'gemini-3.6-flash';
  });
  const [systemPrompt, setSystemPrompt] = useState(
    () => localStorage.getItem('kripibot_system_prompt') || 'You are KripiBot, a brilliant, friendly, and helpful AI assistant.'
  );

  const showError = (msg) => {
    setErrorMessage(msg);
    setTimeout(() => setErrorMessage(null), 6000);
  };

  // Load conversations on mount
  useEffect(() => {
    loadConversations();
  }, []);

  const loadConversations = async () => {
    try {
      const data = await api.getConversations();
      setConversations(data);
    } catch (err) {
      console.warn('Could not load conversations from server:', err);
    }
  };

  // Switch conversation
  const handleSelectConversation = async (id) => {
    if (id === currentConvId) return;
    try {
      setIsLoading(true);
      setCurrentConvId(id);
      const convData = await api.getConversation(id);
      setMessages(convData.messages || []);
      if (convData.modelUsed) {
        setModel(convData.modelUsed);
      }
    } catch (err) {
      showError('Failed to load conversation messages.');
    } finally {
      setIsLoading(false);
    }
  };

  // Start new chat
  const handleNewChat = () => {
    setCurrentConvId(null);
    setMessages([]);
    setInput('');
  };

  // Send message
  const handleSendMessage = async () => {
    if (!input.trim() || isLoading) return;

    const userText = input.trim();
    setInput('');

    // Optimistic user message update
    const tempUserMsg = {
      id: `temp-${Date.now()}`,
      role: 'USER',
      content: userText,
      createdAt: new Date().toISOString(),
    };
    setMessages((prev) => [...prev, tempUserMsg]);
    setIsLoading(true);

    try {
      const res = await api.sendMessage({
        conversationId: currentConvId,
        message: userText,
        model,
        systemPrompt,
      });

      const botMsg = {
        id: res.botMessageId,
        role: 'MODEL',
        content: res.response,
        candidateTokens: res.candidateTokens,
        createdAt: new Date().toISOString(),
      };

      setMessages((prev) => [...prev, botMsg]);

      // If this was a new conversation, update current ID and reload conversation list
      if (!currentConvId && res.conversationId) {
        setCurrentConvId(res.conversationId);
      }
      await loadConversations();
    } catch (err) {
      showError(err.message || 'Failed to send message.');
    } finally {
      setIsLoading(false);
    }
  };

  // Delete conversation
  const handleDeleteConversation = async (id) => {
    try {
      await api.deleteConversation(id);
      setConversations((prev) => prev.filter((c) => c.id !== id));
      if (currentConvId === id) {
        handleNewChat();
      }
    } catch (err) {
      showError('Failed to delete conversation.');
    }
  };

  // Rename conversation
  const handleRenameConversation = async (id, newTitle) => {
    try {
      await api.updateTitle(id, newTitle);
      setConversations((prev) =>
        prev.map((c) => (c.id === id ? { ...c, title: newTitle } : c))
      );
    } catch (err) {
      showError('Failed to rename conversation.');
    }
  };

  const currentConversation = conversations.find((c) => c.id === currentConvId);

  return (
    <div className="app-container">
      <Sidebar
        conversations={conversations}
        currentConvId={currentConvId}
        onSelectConversation={handleSelectConversation}
        onNewChat={handleNewChat}
        onDeleteConversation={handleDeleteConversation}
        onRenameConversation={handleRenameConversation}
        onOpenSettings={() => setIsSettingsOpen(true)}
        isCollapsed={isSidebarCollapsed}
        onToggleCollapse={() => setIsSidebarCollapsed(!isSidebarCollapsed)}
      />

      <ChatArea
        currentConversation={currentConversation}
        messages={messages}
        isLoading={isLoading}
        input={input}
        setInput={setInput}
        onSend={handleSendMessage}
        model={model}
        isSidebarCollapsed={isSidebarCollapsed}
        onToggleSidebar={() => setIsSidebarCollapsed(!isSidebarCollapsed)}
        onOpenSettings={() => setIsSettingsOpen(true)}
      />

      <SettingsModal
        isOpen={isSettingsOpen}
        onClose={() => setIsSettingsOpen(false)}
        model={model}
        setModel={setModel}
        systemPrompt={systemPrompt}
        setSystemPrompt={setSystemPrompt}
      />

      {errorMessage && (
        <div className="toast">
          <AlertCircle size={18} style={{ color: 'var(--danger)', flexShrink: 0 }} />
          <span>{errorMessage}</span>
        </div>
      )}
    </div>
  );
}
