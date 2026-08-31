import React, { useRef, useEffect } from 'react';
import { SendHorizontal, Loader2 } from 'lucide-react';

export default function ChatInput({ input, setInput, onSend, isLoading }) {
  const textareaRef = useRef(null);

  useEffect(() => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto';
      textareaRef.current.style.height = `${Math.min(textareaRef.current.scrollHeight, 160)}px`;
    }
  }, [input]);

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      if (input.trim() && !isLoading) {
        onSend();
      }
    }
  };

  return (
    <div className="input-container">
      <div className="input-box-wrapper">
        <textarea
          ref={textareaRef}
          className="chat-textarea"
          rows={1}
          placeholder="Ask KripiBot anything... (Enter to send, Shift+Enter for new line)"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={isLoading}
        />
        <button
          className="send-btn"
          onClick={onSend}
          disabled={!input.trim() || isLoading}
          title="Send message"
        >
          {isLoading ? (
            <Loader2 size={18} className="animate-spin" style={{ animation: 'spin 1s linear infinite' }} />
          ) : (
            <SendHorizontal size={18} />
          )}
        </button>
      </div>
      <div className="input-footer-hint">
        KripiBot uses Google Gemini & PostgreSQL. Free tier limits may apply.
      </div>
    </div>
  );
}
