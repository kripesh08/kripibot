import React, { useRef, useEffect } from 'react';
import { Bot, Sparkles, PanelLeftOpen, Settings as SettingsIcon, Code, Lightbulb, BookOpen, Layers } from 'lucide-react';
import MessageItem from './MessageItem';
import ChatInput from './ChatInput';

export default function ChatArea({
  currentConversation,
  messages,
  isLoading,
  input,
  setInput,
  onSend,
  model,
  isSidebarCollapsed,
  onToggleSidebar,
  onOpenSettings,
}) {
  const messagesEndRef = useRef(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isLoading]);

  const suggestions = [
    {
      icon: <Code size={18} style={{ color: '#818cf8' }} />,
      title: 'Spring Boot & React App',
      prompt: 'Show me how to build a RESTful CRUD API in Spring Boot with PostgreSQL.',
    },
    {
      icon: <Lightbulb size={18} style={{ color: '#fbbf24' }} />,
      title: 'Explain Complex Topics',
      prompt: 'Explain the difference between SQL and NoSQL databases with pros and cons.',
    },
    {
      icon: <Layers size={18} style={{ color: '#34d399' }} />,
      title: 'System Architecture',
      prompt: 'Design a scalable real-time chat architecture using WebSockets and microservices.',
    },
    {
      icon: <BookOpen size={18} style={{ color: '#f472b6' }} />,
      title: 'Debug & Optimize Code',
      prompt: 'How can I optimize slow JPA queries with Hibernate indexing and fetch joins?',
    },
  ];

  return (
    <main className="main-chat">
      {/* Top Header Bar */}
      <header className="chat-header">
        <div className="header-left">
          {isSidebarCollapsed && (
            <button
              className="icon-btn"
              onClick={onToggleSidebar}
              title="Open Sidebar"
            >
              <PanelLeftOpen size={18} />
            </button>
          )}
          <div className="header-title-wrapper">
            <h2 className="header-title">
              {currentConversation ? currentConversation.title : 'New Chat'}
            </h2>
            <span className="header-subtitle">
              Powered by Google Gemini & Spring Boot
            </span>
          </div>
        </div>

        <div className="header-badges">
          <div className="badge badge-model">
            <Sparkles size={12} />
            <span>{model}</span>
          </div>
          <div className="badge badge-status">
            <span>● Connected</span>
          </div>
          <button className="icon-btn" onClick={onOpenSettings} title="Settings">
            <SettingsIcon size={18} />
          </button>
        </div>
      </header>

      {/* Messages Scroll View */}
      <div className="messages-container">
        {messages.length === 0 ? (
          <div className="empty-state">
            <div className="empty-logo">
              <Bot size={34} />
            </div>
            <h1 className="empty-title">Hello! I'm KripiBot</h1>
            <p className="empty-description">
              Your full-stack AI assistant powered by Google Gemini, Spring Boot 3, and PostgreSQL.
              Select a suggested topic or ask any question to get started.
            </p>

            <div className="suggestion-grid">
              {suggestions.map((item, idx) => (
                <div
                  key={idx}
                  className="suggestion-card"
                  onClick={() => {
                    setInput(item.prompt);
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    {item.icon}
                    <span className="sug-title">{item.title}</span>
                  </div>
                  <span className="sug-prompt">{item.prompt}</span>
                </div>
              ))}
            </div>
          </div>
        ) : (
          <div className="messages-inner">
            {messages.map((msg) => (
              <MessageItem key={msg.id || `${msg.role}-${msg.createdAt}`} message={msg} />
            ))}

            {isLoading && (
              <div className="message-wrapper model">
                <div className="message-avatar bot">
                  <Sparkles size={18} />
                </div>
                <div className="message-bubble-wrapper">
                  <div className="message-bubble">
                    <div className="typing-indicator">
                      <div className="typing-dot" />
                      <div className="typing-dot" />
                      <div className="typing-dot" />
                    </div>
                  </div>
                </div>
              </div>
            )}
            <div ref={messagesEndRef} />
          </div>
        )}
      </div>

      {/* Input Box */}
      <ChatInput
        input={input}
        setInput={setInput}
        onSend={onSend}
        isLoading={isLoading}
      />
    </main>
  );
}
